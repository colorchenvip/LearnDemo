package com.colorchen.mvp.player;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.InflateException;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.colorchen.mvp.player.base.PlayerView;
import com.colorchen.mvp.player.manager.WZMediaManager;

import java.lang.reflect.Constructor;


/**
 * 全屏播放器
 * Created by color on 16/7/19 15:15.
 */
public class PlayerFullScreenActivity extends AppCompatActivity {

    public static void startActivityFromNormal(Context context, PlayerState state,
                                               String url, Class videoPlayClass, Object... object){
        playerState = state;
        URL = url;
        directFullscreen = false;
        videoPlayerClass = videoPlayClass;
        objects = object;
        context.startActivity(new Intent(context, PlayerFullScreenActivity.class));
    }

    /**
     * <p>直接进入全屏播放</p>
     * <p>Full screen play video derictly</p>
     *
     * @param context        context
     * @param url            video mUrl
     * @param videoPlayClass your videoPlayer extends WZAbstractVideoPlayer
     * @param obj            custom param
     */
    public static void startActivity(Context context, String url, Class videoPlayClass, Object... obj) {
        playerState = PlayerState.NORMAL;
        URL = url;
        directFullscreen = true;
        videoPlayerClass = videoPlayClass;
        objects = obj;
        context.startActivity(new Intent(context,PlayerFullScreenActivity.class));
    }

    /**
     * 刚启动全屏时的播放状态
     */
    public static PlayerState playerState = PlayerState.IDLE;
    public static String URL;
    public static boolean directFullscreen = false;//this is should be in videoPlayer
    public static Class videoPlayerClass;
    private static Object[] objects;
    private PlayerView playerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try{
            Constructor<PlayerView> constructor = videoPlayerClass.getConstructor(Context.class);
            playerView = constructor.newInstance(this);
            setContentView(playerView);
        }catch(InflateException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        playerView.mIfCurrentIsFullscreen = true;
        playerView.mIfFullscreenIsDirectly = directFullscreen;
        playerView.setUp(URL, objects);
        playerView.setStateAndUi(playerState);
        playerView.addTextureView();
        if (playerView.mIfFullscreenIsDirectly){
            playerView.startButton.performClick();
        }else{
            PlayerView.ifReleaseWhenOnPause = true;
            WZMediaManager.instance().listener = playerView;
            if (playerState == PlayerState.PAUSE){
                WZMediaManager.instance().mediaPlayer.seekTo(WZMediaManager.instance().mediaPlayer.getCurrentPosition());
            }
        }
    }

    @Override
    public void onBackPressed() {
        playerView.backFullscreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlayerView.releaseAllVideos();
    }
}
