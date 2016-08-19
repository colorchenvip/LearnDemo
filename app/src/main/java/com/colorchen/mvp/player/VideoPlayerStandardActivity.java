package com.colorchen.mvp.player;

import android.net.Uri;

import com.colorchen.R;
import com.colorchen.mvp.player.base.PlayerView;
import com.colorchen.mvp.player.interf.MediaBuriedPointStandard;
import com.colorchen.ui.BaseActivity;

/**
 * 标准的视频播放器
 * Created by color on 16/7/18 10:48.
 */
public class VideoPlayerStandardActivity extends BaseActivity {

    private Uri mVideoUri = null;
    private PlayerStandardView mPlayer;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.video_player_standard_activity;
    }

    /**
     * 初始化VIEW 界面
     */
    @Override
    protected void initViews() {
        mPlayer = (PlayerStandardView) findViewById(R.id.wz_im_video_player);

        mVideoUri = getIntent().getData();
        mPlayer.setUp(mVideoUri.toString(), " ");
        PlayerStandardView.setMediaBuriedPointStandard(buriedPointStandard);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPlayer.startPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlayerView.releaseAllVideos();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        mPlayer.removeAllViews();
    }


    MediaBuriedPointStandard.Stub buriedPointStandard = new MediaBuriedPointStandard.Stub() {
        @Override
        public void onAutoComplete(String url, Object... objects) {
            mPlayer.startPlay();
        }
    };
}
