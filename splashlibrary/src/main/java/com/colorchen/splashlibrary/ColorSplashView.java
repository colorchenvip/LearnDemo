package com.colorchen.splashlibrary;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 运营闪屏
 * des:实现网络首次默认图，静默加载，缓存，二次显示加载图；
 * Author ChenQ on 2017/7/27
 * email：wxchenq@yutong.com
 */
public class ColorSplashView extends FrameLayout{

    private Activity mActivity = null;
    private static final String IMG_URL = "splash_img_url";//图片的地址
    private static final String ACT_URL = "splash_act_url";//提供跳转的地址
    private static String IMG_PATH = null;
    private static final String SP_NAME = "splash";//sp缓存的key
    private static final int skipButtonSizeInDip = 36;
    private static final int skipButtonMarginInDip = 16;
    private Integer duration = 6;//默认页面持续时间
    private static final int delayTime = 1000; // 每隔1000 毫秒执行一次

    private String imgUrl = null;
    private String actUrl = null;

    private boolean isActionBarShowing = true;
    private ImageView splashImageView;
    private TextView skipButton;
    private GradientDrawable splashSkipButtonBg = new GradientDrawable();
    private OnSplashViewActionListener mOnSplashViewActionListener = null;


    public ColorSplashView(Context context) {
        super(context);
        initComponents(context);
    }

    public ColorSplashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponents(context);
    }

    public ColorSplashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponents(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorSplashView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponents(context);
    }

    void initComponents(Context context) {
        this.mActivity = (Activity) context;
        splashSkipButtonBg.setShape(GradientDrawable.OVAL);
        splashSkipButtonBg.setColor(Color.parseColor("#66333333"));

        splashImageView = new ImageView(mActivity);
        splashImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        splashImageView.setBackgroundColor(mActivity.getResources().getColor(android.R.color.white));
        LayoutParams imageViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(splashImageView, imageViewLayoutParams);
        splashImageView.setClickable(true);

        skipButton = new TextView(mActivity);
        int skipButtonSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, skipButtonSizeInDip, mActivity.getResources().getDisplayMetrics());
        LayoutParams skipButtonLayoutParams = new LayoutParams(skipButtonSize, skipButtonSize);
        skipButtonLayoutParams.gravity = Gravity.TOP|Gravity.RIGHT;
        int skipButtonMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, skipButtonMarginInDip, mActivity.getResources().getDisplayMetrics());
        skipButtonLayoutParams.setMargins(0, skipButtonMargin, skipButtonMargin, 0);
        skipButton.setGravity(Gravity.CENTER);
        skipButton.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        skipButton.setBackgroundDrawable(splashSkipButtonBg);
        skipButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        this.addView(skipButton, skipButtonLayoutParams);

        skipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSplashView(true);
            }
        });

        setDuration(duration);
        handler.postDelayed(timerRunnable, delayTime);
    }

    private Handler handler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (0 == duration) {
                dismissSplashView(false);
                return;
            } else {
                setDuration(--duration);
            }
            handler.postDelayed(timerRunnable, delayTime);
        }
    };

    private void setImage(Bitmap image) {
        splashImageView.setImageBitmap(image);
    }

    private void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    private void setDuration(Integer duration) {
        this.duration = duration;
        skipButton.setText(String.format("跳过\n%d s", duration));
    }
    private void setOnSplashImageClickListener(@Nullable final OnSplashViewActionListener listener) {
        if (null == listener) return;
        mOnSplashViewActionListener = listener;
        splashImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSplashImageClick(actUrl);
            }
        });
    }

    public static void showSplashView(@NonNull Activity activity,
                                      @Nullable Integer durationTime,
                                      @Nullable Integer defaultBitmapRes,
                                      @Nullable OnSplashViewActionListener listener) {

        ViewGroup contentView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (null == contentView || 0 == contentView.getChildCount()) {
            throw new IllegalStateException("You should call showSplashView() after setContentView() in Activity instance");
        }
        IMG_PATH = activity.getFilesDir().getAbsolutePath().toString() + "/splash_img.jpg";
        ColorSplashView splashView = new ColorSplashView(activity);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        splashView.setOnSplashImageClickListener(listener);
        if (null != durationTime) splashView.setDuration(durationTime);
        Bitmap bitmapToShow = null;

        if (isExistsLocalSplashData(activity)) {
            bitmapToShow = BitmapFactory.decodeFile(IMG_PATH);
            SharedPreferences sp = activity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            splashView.setImgUrl(sp.getString(IMG_URL, null));
            splashView.setActUrl(sp.getString(ACT_URL, null));
        } else if (null != defaultBitmapRes) {
            bitmapToShow = BitmapFactory.decodeResource(activity.getResources(), defaultBitmapRes);
        }

        if (null == bitmapToShow) return;
        splashView.setImage(bitmapToShow);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (activity instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (null != supportActionBar) {
                supportActionBar.setShowHideAnimationEnabled(false);
                splashView.isActionBarShowing = supportActionBar.isShowing();
                supportActionBar.hide();
            }
        } else if (activity instanceof Activity) {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (null != actionBar) {
                splashView.isActionBarShowing = actionBar.isShowing();
                actionBar.hide();
            }
        }
        contentView.addView(splashView, param);
    }

    public static void simpleShowSplashView(@NonNull Activity activity) {
        showSplashView(activity, null, null, null);
    }

    private void dismissSplashView(boolean initiativeDismiss) {
        if (null != mOnSplashViewActionListener) mOnSplashViewActionListener.onSplashViewDismiss(initiativeDismiss);


        handler.removeCallbacks(timerRunnable);
        final ViewGroup parent = (ViewGroup) this.getParent();
        if (null != parent) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(ColorSplashView.this, "scale", 0.0f, 0.5f).setDuration(600);
            animator.start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (Float) animation.getAnimatedValue();
                    ColorSplashView.this.setAlpha(1.0f - 2.0f * cVal);
                    ColorSplashView.this.setScaleX(1.0f + cVal);
                    ColorSplashView.this.setScaleY(1.0f + cVal);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    parent.removeView(ColorSplashView.this);
                    showSystemUi();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    parent.removeView(ColorSplashView.this);
                    showSystemUi();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    /**
     * 获得系统级rootView 设置为全屏
     */
    private void showSystemUi() {
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (mActivity instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
            if (null != supportActionBar) {
                if (isActionBarShowing) supportActionBar.show();
            }
        } else if (mActivity instanceof Activity) {
            android.app.ActionBar actionBar = mActivity.getActionBar();
            if (null != actionBar) {
                if (isActionBarShowing) actionBar.show();
            }
        }
    }

    private static boolean isExistsLocalSplashData(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String imgUrl = sp.getString(IMG_URL, null);
        return !TextUtils.isEmpty(imgUrl) && isFileExist(IMG_PATH);
    }

    public static void updateSplashData(@NonNull Activity activity, @NonNull String imgUrl, @Nullable String actionUrl) {
        IMG_PATH = activity.getFilesDir().getAbsolutePath().toString() + "/splash_img.jpg";

        SharedPreferences.Editor editor = activity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(IMG_URL, imgUrl);
        editor.putString(ACT_URL, actionUrl);
        editor.apply();

        getAndSaveNetWorkBitmap(imgUrl);
    }

    private static void getAndSaveNetWorkBitmap(final String urlString) {
        Runnable getAndSaveImageRunnable = new Runnable() {
            @Override
            public void run() {
                URL imgUrl = null;
                Bitmap bitmap = null;
                try {
                    imgUrl = new URL(urlString);
                    HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.connect();
                    InputStream is = urlConn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    saveBitmapFile(bitmap, IMG_PATH);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(getAndSaveImageRunnable).start();
    }

    private static void saveBitmapFile(Bitmap bm, String filePath) throws IOException {
        File myCaptureFile = new File(filePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    public static boolean isFileExist(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            return false;
        } else {
            File file = new File(filePath);
            return file.exists() && file.isFile();
        }
    }
    public interface OnSplashViewActionListener {
        void onSplashImageClick(String actionUrl);
        void onSplashViewDismiss(boolean initiativeDismiss);
    }
}
