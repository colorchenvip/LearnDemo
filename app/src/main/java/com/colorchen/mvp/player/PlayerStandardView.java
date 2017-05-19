package com.colorchen.mvp.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.colorchen.R;
import com.colorchen.mvp.player.base.PlayerView;
import com.colorchen.mvp.player.interf.MediaBuriedPointStandard;
import com.colorchen.mvp.player.manager.WZMediaManager;
import com.colorchen.utils.NetStateUtils;
import com.colorchen.utils.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 标准播放器
 * Created by color on 16/7/20 10:42.
 */
public class PlayerStandardView extends PlayerView {

    public ImageView backButton;
    public ProgressBar bottomProgressBar, loadingProgressBar;
    public TextView titleTextView;
    public ImageView thumbImageView;
    public ImageView coverImageView;

    protected static Timer dismissControlViewTimer;
    protected static MediaBuriedPointStandard mediaBuriedPointStandard;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;

    public PlayerStandardView(Context context) {
        super(context);
        init(context);
    }

    public PlayerStandardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.player_media;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        bottomProgressBar = (ProgressBar) findViewById(R.id.bottom_progressbar);
        titleTextView = (TextView) findViewById(R.id.mediaPlayerTitle);
        backButton = (ImageView) findViewById(R.id.mediaPlayerBack);
        thumbImageView = (ImageView) findViewById(R.id.thumb);
        coverImageView = (ImageView) findViewById(R.id.cover);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading);

        thumbImageView.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public boolean setUp(String url, Object... mObject) {
        if (mObject.length != 0 && super.setUp(url,mObject)){
            String titleName = mObject[0].toString();
            if (StringUtils.isBlank(titleName)){
                topContainer.setVisibility(GONE);
            }else {
                titleTextView.setText(titleName);
            }
            if (mIfCurrentIsFullscreen){
                fullscreenButton.setActivated(true);
            }else{
                fullscreenButton.setActivated(false);
                backButton.setVisibility(GONE);
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void setStateAndUi(PlayerState state) {
        super.setStateAndUi(state);
        switch (mCurrentState) {
            case NORMAL:
                changeUiToNormal();
                break;
            case PREPARING:
                changeUiToPreparingShow();
                startDismissControlViewTimer();
                break;
            case PLAYING:
                changeUiToPlayingShow();
                startDismissControlViewTimer();
                break;
            case PAUSE:
                changeUiToPauseShow();
                cancelDismissControlViewTimer();
                break;
            case ERROR:
                changeUiToError();
                break;
            case AUTO_COMPLETE:
                changeUiToCompleteShow();
                cancelDismissControlViewTimer();
                bottomProgressBar.setProgress(100);
                break;
            case PLAYING_BUFFERING_START:
                changeUiToPlayingBufferingShow();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.thumb) {
            if (TextUtils.isEmpty(mUrl)) {
                return;
            }
            if (mCurrentState == PlayerState.NORMAL) {
                if (!NetStateUtils.isWifiNetworkConnected(getContext()) && !wifiTipDialogShowed) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("没有使用WIFI是否继续播放?");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startPreparePlay();
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
                startPreparePlay();
            } else if (mCurrentState == PlayerState.AUTO_COMPLETE) {
                onClickUiToggle();
            }
        } else if (i == R.id.surface_container) {
            if (mediaBuriedPointStandard != null && WZMediaManager.instance().listener == this) {
                if (mIfCurrentIsFullscreen) {
                    mediaBuriedPointStandard.onClickBlankFullscreen(mUrl, mObjects);
                } else {
                    mediaBuriedPointStandard.onClickBlank(mUrl, mObjects);
                }
            }
            startDismissControlViewTimer();
        } else if (i == R.id.mediaPlayerBack) {
            backFullscreen();
        }
    }

    private void startPreparePlay() {
        if (mediaBuriedPointStandard != null) {
            mediaBuriedPointStandard.onClickStartThumb(mUrl, mObjects);
        }
        prepareVideo();
        startDismissControlViewTimer();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    if (mChangePosition) {
                        int duration = getDuration();
                        int progress = mSeekTimePosition * 100 / (duration == 0 ? 1 : duration);
                        bottomProgressBar.setProgress(progress);
                    }
                    if (!mChangePosition && !mChangeVolume) {
                        onClickUiToggle();
                    }
                    break;
            }
        } else if (id == R.id.seek_bar) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    break;
            }
        }
        return super.onTouch(v, event);
    }

    private void onClickUiToggle() {
        if (mCurrentState == PlayerState.PREPARING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPreparingClear();
            } else {
                changeUiToPreparingShow();
            }
        } else if (mCurrentState == PlayerState.PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (mCurrentState == PlayerState.PAUSE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        } else if (mCurrentState == PlayerState.AUTO_COMPLETE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToCompleteClear();
            } else {
                changeUiToCompleteShow();
            }
        } else if (mCurrentState ==PlayerState.PLAYING_BUFFERING_START) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingBufferingClear();
            } else {
                changeUiToPlayingBufferingShow();
            }
        }
    }

    @Override
    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime);
        if (progress != 0) bottomProgressBar.setProgress(progress);
        if (secProgress != 0) bottomProgressBar.setSecondaryProgress(secProgress);
    }

    @Override
    protected void resetProgressAndTime() {
        super.resetProgressAndTime();
        bottomProgressBar.setProgress(0);
        bottomProgressBar.setSecondaryProgress(0);
    }


    private void changeUiToNormal() {
        topContainer.setVisibility(View.VISIBLE);
        bottomContainer.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        thumbImageView.setVisibility(View.VISIBLE);
        coverImageView.setVisibility(View.VISIBLE);
        bottomProgressBar.setVisibility(View.INVISIBLE);
        updateStartImage();
    }

    private void changeUiToPreparingShow() {
        topContainer.setVisibility(View.VISIBLE);
        bottomContainer.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        loadingProgressBar.setVisibility(View.VISIBLE);
        thumbImageView.setVisibility(View.INVISIBLE);
        coverImageView.setVisibility(View.VISIBLE);
        bottomProgressBar.setVisibility(View.INVISIBLE);
    }

    private void changeUiToPreparingClear() {
        topContainer.setVisibility(View.INVISIBLE);
        bottomContainer.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        thumbImageView.setVisibility(View.INVISIBLE);
        bottomProgressBar.setVisibility(View.INVISIBLE);
        coverImageView.setVisibility(View.VISIBLE);
    }

    private void changeUiToPlayingShow() {
        topContainer.setVisibility(View.VISIBLE);
        bottomContainer.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        thumbImageView.setVisibility(View.INVISIBLE);
        coverImageView.setVisibility(View.INVISIBLE);
        bottomProgressBar.setVisibility(View.INVISIBLE);
        fullscreenButton.setVisibility(VISIBLE);
        updateStartImage();
    }

    private void changeUiToPlayingClear() {
        changeUiToClear();
        bottomProgressBar.setVisibility(View.VISIBLE);
    }

    private void changeUiToPauseShow() {
        topContainer.setVisibility(View.VISIBLE);
        bottomContainer.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        thumbImageView.setVisibility(View.INVISIBLE);
        coverImageView.setVisibility(View.INVISIBLE);
        bottomProgressBar.setVisibility(View.INVISIBLE);
        updateStartImage();
    }

    private void changeUiToPauseClear() {
        changeUiToClear();
        bottomProgressBar.setVisibility(View.VISIBLE);
    }

    private void changeUiToPlayingBufferingShow() {
        topContainer.setVisibility(View.VISIBLE);
        bottomContainer.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        loadingProgressBar.setVisibility(View.VISIBLE);
        thumbImageView.setVisibility(View.INVISIBLE);
        coverImageView.setVisibility(View.INVISIBLE);
        bottomProgressBar.setVisibility(View.INVISIBLE);
    }

    private void changeUiToPlayingBufferingClear() {
        topContainer.setVisibility(View.INVISIBLE);
        bottomContainer.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        loadingProgressBar.setVisibility(View.VISIBLE);
        thumbImageView.setVisibility(View.INVISIBLE);
        coverImageView.setVisibility(View.INVISIBLE);
        bottomProgressBar.setVisibility(View.VISIBLE);
        updateStartImage();
    }

    private void changeUiToClear() {
        topContainer.setVisibility(View.INVISIBLE);
        bottomContainer.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        thumbImageView.setVisibility(View.INVISIBLE);
        coverImageView.setVisibility(View.INVISIBLE);
        bottomProgressBar.setVisibility(View.INVISIBLE);
    }

    private void changeUiToCompleteShow() {
        topContainer.setVisibility(View.VISIBLE);
        bottomContainer.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        thumbImageView.setVisibility(View.VISIBLE);
        coverImageView.setVisibility(View.INVISIBLE);
        bottomProgressBar.setVisibility(View.INVISIBLE);
        fullscreenButton.setVisibility(GONE);
        updateStartImage();
    }

    private void changeUiToCompleteClear() {
        topContainer.setVisibility(View.INVISIBLE);
        bottomContainer.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        thumbImageView.setVisibility(View.VISIBLE);
        coverImageView.setVisibility(View.INVISIBLE);
        bottomProgressBar.setVisibility(View.VISIBLE);
        updateStartImage();
    }

    private void changeUiToError() {
        topContainer.setVisibility(View.INVISIBLE);
        bottomContainer.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        thumbImageView.setVisibility(View.INVISIBLE);
        coverImageView.setVisibility(View.VISIBLE);
        bottomProgressBar.setVisibility(View.INVISIBLE);
        updateStartImage();
    }

    private void updateStartImage() {
        if (mCurrentState == PlayerState.PREPARING) {
            startButton.setVisibility(GONE);
        } else if (mCurrentState == PlayerState.ERROR) {
            startButton.setVisibility(VISIBLE);
        } else {
            startButton.setVisibility(GONE);
        }
    }

    protected Dialog mProgressDialog;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogIcon;

    @Override
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
        if (mProgressDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.player_progress_dialog, null);
            mDialogProgressBar = ((ProgressBar) localView.findViewById(R.id.duration_progressbar));
            mDialogSeekTime = ((TextView) localView.findViewById(R.id.tv_current));
            mDialogTotalTime = ((TextView) localView.findViewById(R.id.tv_duration));
            mDialogIcon = ((ImageView) localView.findViewById(R.id.duration_image_tip));
            mProgressDialog = new Dialog(getContext(), R.style.jc_style_dialog_progress);
            mProgressDialog.setContentView(localView);
            mProgressDialog.getWindow().addFlags(Window.FEATURE_ACTION_BAR);
            mProgressDialog.getWindow().addFlags(32);
            mProgressDialog.getWindow().addFlags(16);
            mProgressDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mProgressDialog.getWindow().getAttributes();
            localLayoutParams.gravity = 49;
            localLayoutParams.y = 80;
            mProgressDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        mDialogSeekTime.setText(seekTime);
        mDialogTotalTime.setText(" / " + totalTime);
        mDialogProgressBar.setProgress(seekTimePosition * 100 / totalTimeDuration);
        if (deltaX > 0) {
            mDialogIcon.setBackgroundResource(R.mipmap.jc_forward_icon);
        } else {
            mDialogIcon.setBackgroundResource(R.mipmap.jc_backward_icon);
        }
    }

    @Override
    protected void dismissProgressDialog() {
        super.dismissProgressDialog();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected Dialog mVolumeDialog;
    protected ProgressBar mDialogVolumeProgressBar;

    @Override
    protected void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        if (mVolumeDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.player_volume_dialog, null);
            mDialogVolumeProgressBar = ((ProgressBar) localView.findViewById(R.id.volume_progressbar));
            mVolumeDialog = new Dialog(getContext(), R.style.jc_style_dialog_progress);
            mVolumeDialog.setContentView(localView);
            mVolumeDialog.getWindow().addFlags(8);
            mVolumeDialog.getWindow().addFlags(32);
            mVolumeDialog.getWindow().addFlags(16);
            mVolumeDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mVolumeDialog.getWindow().getAttributes();
            localLayoutParams.gravity = 19;
            localLayoutParams.x = 24;
            mVolumeDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mVolumeDialog.isShowing()) {
            mVolumeDialog.show();
        }

        mDialogVolumeProgressBar.setProgress(volumePercent);
    }

    @Override
    protected void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (mVolumeDialog != null) {
            mVolumeDialog.dismiss();
        }
    }


    private void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        dismissControlViewTimer = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        dismissControlViewTimer.schedule(mDismissControlViewTimerTask, 2500);
    }

    private void cancelDismissControlViewTimer() {
        if (dismissControlViewTimer != null) {
            dismissControlViewTimer.cancel();
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
        }
    }

    protected class DismissControlViewTimerTask extends TimerTask {

        @Override
        public void run() {
            if (mCurrentState != PlayerState.NORMAL
                    && mCurrentState != PlayerState.ERROR
                    && mCurrentState != PlayerState.AUTO_COMPLETE) {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bottomContainer.setVisibility(View.INVISIBLE);
                            topContainer.setVisibility(View.INVISIBLE);
                            bottomProgressBar.setVisibility(View.VISIBLE);
                            startButton.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }
    }

    /**
     * 在UI中设置一下,在回调中实现相应的功能即可
     * @param jcBuriedPointStandard
     */
    public static void setMediaBuriedPointStandard(MediaBuriedPointStandard jcBuriedPointStandard) {
        mediaBuriedPointStandard = jcBuriedPointStandard;
        PlayerView.setMediaBuriedPoint(jcBuriedPointStandard);
    }

}
