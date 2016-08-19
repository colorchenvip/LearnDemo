package com.colorchen.mvp.player.interf;

/**
 * Created by color on 16/7/18 16:45.
 */
public interface WZMediaPlayerListener {
    void onPrepared();

    void onAutoCompletion();

    void onCompletion();

    void onBufferingUpdate(int percent);

    void onSeekComplete();

    void onError(int what, int extra);

    void onInfo(int what, int extra);

    void onVideoSizeChanged();

    void onBackFullscreen();
}
