package com.colorchen.mvp.player.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.colorchen.R;
import com.colorchen.mvp.player.PlayerFullScreenActivity;
import com.colorchen.mvp.player.PlayerState;
import com.colorchen.mvp.player.interf.MediaBuriedPoint;
import com.colorchen.mvp.player.interf.WZMediaPlayerListener;
import com.colorchen.mvp.player.manager.WZMediaManager;
import com.colorchen.utils.NetStateUtils;
import com.colorchen.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by color on 16/7/18 16:06.
 */
public abstract class PlayerView extends FrameLayout implements View.OnTouchListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, WZMediaPlayerListener, TextureView.SurfaceTextureListener {
    public static final String TAG = "PlayerView";
    protected boolean mTouchingProgressBar = false;
    public boolean mIfCurrentIsFullscreen = false;
    public boolean mIfFullscreenIsDirectly = false;//mIfCurrentIsFullscreen should be true first
    public static boolean ifReleaseWhenOnPause = true;
    protected static boolean ifFullScreenFromNormal = false;//to prevent infinite looping
    protected static long clickQuitFullScreenTime = 0;
    protected static final int FULL_SCREEN_NORMAL_DELAY = 2000;

    public ImageView startButton;
    public ImageView playButton;
    public SeekBar progressBar;
    public ImageView fullscreenButton;
    public TextView currentTimeTextView, totalTimeTextView;
    public ViewGroup textureViewContainer;
    public ViewGroup topContainer, bottomContainer;
    public WZResizeTextureView textureView;
    public Surface mSurface;

    protected String mUrl;
    protected Object[] mObjects;
    protected Map<String, String> mMapHeadData = new HashMap<>();
    protected boolean mLooping = false;

    protected Timer updateProgressTimer;
    protected ProgressTimerTask mProgressTimerTask;

    protected static MediaBuriedPoint mediaBuriedPoint;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected AudioManager mAudioManager;
    protected int mThreshold = 80;
    protected float mDownX;
    protected float mDownY;
    protected boolean mChangeVolume = false;
    protected boolean mChangePosition = false;
    protected int mDownPosition;
    protected int mGestureDownVolume;

    protected int mSeekTimePosition;//change position when finger up

    protected boolean wifiTipDialogShowed = false;

    protected PlayerState mCurrentState = PlayerState.IDLE;
    private PlayerState BACKUP_PLAYING_BUFFERING_STATE = PlayerState.IDLE;


    public PlayerView(Context context) {
        super(context);
        init(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        startButton = (ImageView) findViewById(R.id.iv_big_play);
        playButton = (ImageView) findViewById(R.id.iv_play);
        fullscreenButton = (ImageView) findViewById(R.id.iv_full_screen);
        progressBar = (SeekBar) findViewById(R.id.seek_bar);
        currentTimeTextView = (TextView) findViewById(R.id.tv_cur_time);
        totalTimeTextView = (TextView) findViewById(R.id.tv_duration);
        bottomContainer = (ViewGroup) findViewById(R.id.layout_bottom);
        textureViewContainer = (RelativeLayout) findViewById(R.id.surface_container);
        topContainer = (ViewGroup) findViewById(R.id.layout_top);

        startButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        fullscreenButton.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);
        bottomContainer.setOnClickListener(this);
        textureViewContainer.setOnClickListener(this);
        progressBar.setOnTouchListener(this);

        textureViewContainer.setOnTouchListener(this);
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    public abstract int getLayoutId();

    protected static void setMediaBuriedPoint(MediaBuriedPoint buriedPoint) {
        mediaBuriedPoint = buriedPoint;
    }

    public boolean setUp(String url, Object... objects) {
        if (WZMediaManager.instance().listener == this &&
                (System.currentTimeMillis() - clickQuitFullScreenTime) < FULL_SCREEN_NORMAL_DELAY) {
            return false;
        }
        mCurrentState = PlayerState.NORMAL;
        this.mUrl = url;
        this.mObjects = objects;
        setStateAndUi(mCurrentState);
        return true;
    }

    public void setStateAndUi(PlayerState state) {
        mCurrentState = state;
        updatePlayImage();
        switch (mCurrentState) {
            case NORMAL:
                if (WZMediaManager.instance().listener == this) {
                    cancelProgressTimer();
                    WZMediaManager.instance().releaseMediaPlayer();
                }
                break;
            case PREPARING:
                resetProgressAndTime();
                break;
            case PLAYING:
                startProgressTimer();
                break;
            case PAUSE:
                startProgressTimer();
                break;
            case ERROR:
                if (WZMediaManager.instance().listener == this) {
                    WZMediaManager.instance().releaseMediaPlayer();
                }
                break;
            case AUTO_COMPLETE:
                cancelProgressTimer();
                progressBar.setProgress(100);
                currentTimeTextView.setText(totalTimeTextView.getText());
                break;
        }
    }

    private void updatePlayImage() {
        if (mCurrentState == PlayerState.PREPARING ) {
            playButton.setActivated(false);
        } else if (mCurrentState == PlayerState.ERROR || mCurrentState == PlayerState.PAUSE) {
            playButton.setActivated(true);
        } else {
            playButton.setActivated(false);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_play || i == R.id.iv_big_play) {

            Log.i(TAG, "--- onClick start ");
            if (TextUtils.isEmpty(mUrl)) {
                return;
            }
            if (mCurrentState == PlayerState.NORMAL || mCurrentState == PlayerState.ERROR) {
                if (!NetStateUtils.isWifiNetworkConnected(getContext()) && !wifiTipDialogShowed) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("没有使用WIFI,是否继续播放?");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startPlay();
                            wifiTipDialogShowed = true;
                        }
                    });
                    builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                    return;
                }
                startPlay();
            } else if (mCurrentState == PlayerState.PLAYING) {
                Log.d(TAG, "-- pauseVideo ");
                WZMediaManager.instance().mediaPlayer.pause();
                setStateAndUi(PlayerState.PAUSE);
                if (mediaBuriedPoint != null && WZMediaManager.instance().listener == this) {
                    if (mIfCurrentIsFullscreen) {
                        mediaBuriedPoint.onClickStopFullscreen(mUrl, mObjects);
                    } else {
                        mediaBuriedPoint.onClickStop(mUrl, mObjects);
                    }
                }

            } else if (mCurrentState == PlayerState.PAUSE) {
                if (mediaBuriedPoint != null) {
                    if (WZMediaManager.instance().listener == this) {
                        mediaBuriedPoint.onClickResumeFullscreen(mUrl, mObjects);
                    } else {
                        mediaBuriedPoint.onClickResume(mUrl, mObjects);
                    }
                }
                WZMediaManager.instance().mediaPlayer.start();
                setStateAndUi(PlayerState.PLAYING);
            } else {
                startPlay();
            }
        } else if (i == R.id.iv_full_screen) {
            Log.i(TAG, "--- onClick fullscreen ");
            if (mCurrentState == PlayerState.AUTO_COMPLETE) {
                return;
            }
            if (mIfCurrentIsFullscreen) {
                backFullscreen();
            } else {
                Log.d(TAG, "--- toFullscreenActivity ");
                if (mediaBuriedPoint != null && WZMediaManager.instance().listener == this) {
                    mediaBuriedPoint.onEnterFullscreen(mUrl, mObjects);
                }
                //to fullscreen
                WZMediaManager.instance().setDisplay(null);
                WZMediaManager.instance().lastListener = this;
                WZMediaManager.instance().listener = null;
                ifFullScreenFromNormal = true;
                ifReleaseWhenOnPause = false;
                PlayerFullScreenActivity.startActivityFromNormal(getContext(), mCurrentState, mUrl, PlayerView.this.getClass(), this.mObjects);
            }
        } else if (i == R.id.surface_container && mCurrentState == PlayerState.ERROR) {
            Log.i(TAG, "--- onClick surfaceContainer State=Error ");
            if (mCurrentState == PlayerState.ERROR) {
                if (mediaBuriedPoint != null) {
                    mediaBuriedPoint.onClickStartError(mUrl, mObjects);
                }
            }
            prepareVideo();
        }
    }

    /**
     * 退出全屏
     */
    public void backFullscreen() {
        Log.d(TAG, "--- quitFullscreen  ");
        ifFullScreenFromNormal = false;
        if (mIfFullscreenIsDirectly) {
            WZMediaManager.instance().mediaPlayer.stop();
            finishFullscreenActivity();
        } else {
            clickQuitFullScreenTime = System.currentTimeMillis();
            ifReleaseWhenOnPause = false;
            quitFullScreenGoToNormal();
        }
    }

    /**
     * 退出全屏回到正常状态播放
     */
    public void quitFullScreenGoToNormal() {
        Log.d(TAG, "quitFullScreenGoToNormal ");
        if (mediaBuriedPoint != null && WZMediaManager.instance().listener == this) {
            mediaBuriedPoint.onQuitFullscreen(mUrl, mObjects);
        }
        WZMediaManager.instance().setDisplay(null);
        WZMediaManager.instance().listener = WZMediaManager.instance().lastListener;
        WZMediaManager.instance().lastListener = null;
        WZMediaManager.instance().lastState = mCurrentState;//save state
        WZMediaManager.instance().listener.onBackFullscreen();
        if (mCurrentState == PlayerState.PAUSE) {
            WZMediaManager.instance().mediaPlayer.seekTo(WZMediaManager.instance().mediaPlayer.getCurrentPosition());
        }
        finishFullscreenActivity();
    }

    /**
     * 结束全屏
     */
    protected void finishFullscreenActivity() {
        if (getContext() instanceof PlayerFullScreenActivity) {
            Log.d(TAG, "finishFullscreenActivity ");
            ((PlayerFullScreenActivity) getContext()).finish();
        }
    }

    public void startPlay() {
        if (mediaBuriedPoint != null && mCurrentState == PlayerState.NORMAL) {
            mediaBuriedPoint.onClickStartIcon(mUrl, mObjects);
        } else if (mediaBuriedPoint != null) {
            mediaBuriedPoint.onClickStartError(mUrl, mObjects);
        }
        prepareVideo();
    }

    protected void prepareVideo() {
        Log.d(TAG, "002 prepareVideo ");
        if (WZMediaManager.instance().listener != null) {
            WZMediaManager.instance().listener.onCompletion();
        }
        WZMediaManager.instance().listener = this;
        addTextureView();
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WZMediaManager.instance().prepare(mUrl, mMapHeadData, mLooping);
        setStateAndUi(PlayerState.PREPARING);
    }

    private static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseAllVideos();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (WZMediaManager.instance().mediaPlayer.isPlaying()) {
                        WZMediaManager.instance().mediaPlayer.pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };

    public static void releaseAllVideos() {
        if (ifReleaseWhenOnPause) {
            Log.d(TAG, "releaseAllVideos");
            if (WZMediaManager.instance().listener != null) {
                WZMediaManager.instance().listener.onCompletion();
            }
            WZMediaManager.instance().releaseMediaPlayer();
        } else {
            ifReleaseWhenOnPause = true;
        }
    }

    public void addTextureView() {
        Log.d(TAG, "003 addTextureView  ");
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        textureView = null;
        textureView = new WZResizeTextureView(getContext());
        textureView.setSurfaceTextureListener(this);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textureViewContainer.addView(textureView, layoutParams);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.i(TAG, "onSurfaceTextureAvailable [" + this.hashCode() + "] ");
        if (mCurrentState != PlayerState.AUTO_COMPLETE){
            mSurface = new Surface(surface);
            WZMediaManager.instance().setDisplay(mSurface);
        }
    }

    protected void startProgressTimer() {
        cancelProgressTimer();
        updateProgressTimer = new Timer();
        mProgressTimerTask = new ProgressTimerTask();
        updateProgressTimer.schedule(mProgressTimerTask, 0, 300);
    }

    protected void cancelProgressTimer() {
        if (updateProgressTimer != null) {
            updateProgressTimer.cancel();
        }
        if (mProgressTimerTask != null) {
            mProgressTimerTask.cancel();
        }
    }

    protected void resetProgressAndTime() {
        progressBar.setProgress(0);
        progressBar.setSecondaryProgress(0);
        currentTimeTextView.setText(TimeUtils.stringForTime(0));
        totalTimeTextView.setText(TimeUtils.stringForTime(0));
    }

    protected class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mCurrentState == PlayerState.PLAYING_BUFFERING_START || mCurrentState == PlayerState.PAUSE) {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTextAndProgress(0);
                        }
                    });
                }
            }
        }
    }

    protected void setTextAndProgress(int secProgress) {
        int position = getCurrentPositionWhenPlaying();
        int duration = getDuration();
        // if duration == 0 (e.g. in HLS streams) avoids ArithmeticException
        int progress = position * 100 / (duration == 0 ? 1 : duration);
        setProgressAndTime(progress, secProgress, position, duration);
    }

    protected int getCurrentPositionWhenPlaying() {
        int position = 0;
        if (mCurrentState == PlayerState.PLAYING || mCurrentState == PlayerState.PAUSE) {
            try {
                position = WZMediaManager.instance().mediaPlayer.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return position;
            }
        }
        return position;
    }

    protected int getDuration() {
        int duration = 0;
        try {
            duration = WZMediaManager.instance().mediaPlayer.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }

    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        if (!mTouchingProgressBar) {
            if (progress != 0) progressBar.setProgress(progress);
        }
        if (secProgress != 0) progressBar.setSecondaryProgress(secProgress);
        currentTimeTextView.setText(TimeUtils.stringForTime(currentTime));
        totalTimeTextView.setText(TimeUtils.stringForTime(totalTime));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, "onTouch surfaceContainer actionDown " );
                    mTouchingProgressBar = true;
                    mDownX = x;
                    mDownY = y;
                    mChangeVolume = false;
                    mChangePosition = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "onTouch surfaceContainer actionMove ");
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if (mIfCurrentIsFullscreen) {
                        if (!mChangePosition && !mChangeVolume) {
                            if (absDeltaX > mThreshold || absDeltaY > mThreshold) {
                                cancelProgressTimer();
                                if (absDeltaX >= mThreshold) {
                                    mChangePosition = true;
                                    mDownPosition = getCurrentPositionWhenPlaying();
                                    if (mediaBuriedPoint != null && WZMediaManager.instance().listener == this) {
                                        mediaBuriedPoint.onTouchScreenSeekPosition(mUrl, mObjects);
                                    }
                                } else {
                                    mChangeVolume = true;
                                    mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                    if (mediaBuriedPoint != null && WZMediaManager.instance().listener == this) {
                                        mediaBuriedPoint.onTouchScreenSeekVolume(mUrl, mObjects);
                                    }
                                }
                            }
                        }
                    }

                    if (mChangePosition) {
                        int totalTimeDuration = getDuration();
                        mSeekTimePosition = (int) (mDownPosition + deltaX * totalTimeDuration / mScreenWidth);
                        if (mSeekTimePosition > totalTimeDuration) {
                            mSeekTimePosition = totalTimeDuration;
                        }
                        String seekTime = TimeUtils.stringForTime(mSeekTimePosition);
                        String totalTime = TimeUtils.stringForTime(totalTimeDuration);

                        showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);
                    }
                    if (mChangeVolume) {
                        deltaY = -deltaY;
                        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
                        int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);

                        showVolumeDialog(-deltaY, volumePercent);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "onTouch surfaceContainer actionUp ");
                    mTouchingProgressBar = false;
                    dismissProgressDialog();
                    dismissVolumeDialog();
                    if (mChangePosition) {
                        WZMediaManager.instance().mediaPlayer.seekTo(mSeekTimePosition);
                        int duration = getDuration();
                        int progress = mSeekTimePosition * 100 / (duration == 0 ? 1 : duration);
                        progressBar.setProgress(progress);
                    }
                    startProgressTimer();
                    if (mediaBuriedPoint != null && WZMediaManager.instance().listener == this) {
                        if (mIfCurrentIsFullscreen) {
                            mediaBuriedPoint.onClickSeekBarFullscreen(mUrl, mObjects);
                        } else {
                            mediaBuriedPoint.onClickSeekBar(mUrl, mObjects);
                        }
                    }
                    break;
            }
        } else if (id == R.id.seek_bar) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, "onTouch bottomProgress actionDown");
                    cancelProgressTimer();
                    ViewParent viewParentDown = getParent();
                    while (viewParentDown != null) {
                        viewParentDown.requestDisallowInterceptTouchEvent(true);
                        viewParentDown = viewParentDown.getParent();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "onTouch bottomProgress actionUp");
                    startProgressTimer();
                    ViewParent viewParentUp = getParent();
                    while (viewParentUp != null) {
                        viewParentUp.requestDisallowInterceptTouchEvent(false);
                        viewParentUp = viewParentUp.getParent();
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mCurrentState != PlayerState.PREPARING && mCurrentState != PlayerState.PAUSE) {
            return;
        }
        if (fromUser) {
            int time = progress * getDuration() / 100;
            WZMediaManager.instance().mediaPlayer.seekTo(time);
            Log.i(TAG, "seekTo " + time + " [" + progress + "] ");
        }
    }

    @Override
    public void onAutoCompletion() {
        if (mediaBuriedPoint != null && WZMediaManager.instance().listener == this) {
            if (mIfCurrentIsFullscreen) {
                mediaBuriedPoint.onAutoCompleteFullscreen(mUrl, mObjects);
            } else {
                mediaBuriedPoint.onAutoComplete(mUrl, mObjects);
            }
        }
        setStateAndUi(PlayerState.AUTO_COMPLETE);
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        finishFullscreenActivity();
        //如果在进入全屏后播放完就初始化自己非全屏的控件
        if (ifFullScreenFromNormal) {
            ifFullScreenFromNormal = false;
            WZMediaManager.instance().lastListener.onAutoCompletion();
        }

        WZMediaManager.instance().lastListener = null;
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onCompletion() {
        setStateAndUi(PlayerState.NORMAL);
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        finishFullscreenActivity();
        //如果在进入全屏后播放完就初始化自己非全屏的控件
        if (ifFullScreenFromNormal) {
            ifFullScreenFromNormal = false;
            WZMediaManager.instance().lastListener.onCompletion();
        }
        WZMediaManager.instance().listener = null;
        WZMediaManager.instance().lastListener = null;
        WZMediaManager.instance().currentVideoHeight = 0;
        WZMediaManager.instance().currentVideoWidth = 0;

        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onError(int what, int extra) {
        Log.e(TAG, "onError " + what + " - " + extra );
        if (what != 38 && what != -38) {
            setStateAndUi(PlayerState.ERROR);
        }
    }

    @Override
    public void onInfo(int what, int extra) {
        Log.d(TAG, "004 onInfo what - " + what + " extra - " + extra);
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            BACKUP_PLAYING_BUFFERING_STATE = mCurrentState;
            setStateAndUi(PlayerState.BACKUP_PLAYING_BUFFERING_STATE);
            Log.d(TAG, "MEDIA_INFO_BUFFERING_START");
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (BACKUP_PLAYING_BUFFERING_STATE != PlayerState.IDLE) {
                setStateAndUi(BACKUP_PLAYING_BUFFERING_STATE);
                BACKUP_PLAYING_BUFFERING_STATE = PlayerState.IDLE;
            }
            Log.d(TAG, "MEDIA_INFO_BUFFERING_END");
        }
    }

    @Override
    public void onVideoSizeChanged() {
        int mVideoWidth = WZMediaManager.instance().currentVideoWidth;
        int mVideoHeight = WZMediaManager.instance().currentVideoHeight;
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            textureView.requestLayout();
        }
    }

    @Override
    public void onBackFullscreen() {
        mCurrentState = WZMediaManager.instance().lastState;
        setStateAndUi(mCurrentState);
        addTextureView();
    }

    protected void showProgressDialog(float deltaX,
                                      String seekTime, int seekTimePosition,
                                      String totalTime, int totalTimeDuration) {
    }

    protected void showVolumeDialog(float deltaY, int volumePercent) {

    }

    protected void dismissProgressDialog() {

    }

    protected void dismissVolumeDialog() {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onPrepared() {
        if (mCurrentState != PlayerState.PREPARING) return;
        WZMediaManager.instance().mediaPlayer.start();
        startProgressTimer();
        setStateAndUi(PlayerState.PLAYING);
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mCurrentState != PlayerState.NORMAL && mCurrentState != PlayerState.PREPARING) {
            setTextAndProgress(percent);
        }
    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        surface.release();
        return true;
    }

}
