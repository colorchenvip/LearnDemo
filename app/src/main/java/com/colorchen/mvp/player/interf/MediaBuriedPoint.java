package com.colorchen.mvp.player.interf;

/**
 * media 播放器接口
 * Created by color on 16/7/18 16:33.
 */
public interface MediaBuriedPoint {
    void onClickStartIcon(String url, Object... objects);

    void onClickStartError(String url, Object... objects);

    void onClickStop(String url, Object... objects);

    void onClickStopFullscreen(String url, Object... objects);

    void onClickResume(String url, Object... objects);

    void onClickResumeFullscreen(String url, Object... objects);

    void onClickSeekBar(String url, Object... objects);

    void onClickSeekBarFullscreen(String url, Object... objects);

    void onAutoComplete(String url, Object... objects);

    void onAutoCompleteFullscreen(String url, Object... objects);

    void onEnterFullscreen(String url, Object... objects);

    void onQuitFullscreen(String url, Object... objects);

    void onTouchScreenSeekVolume(String url, Object... objects);

    void onTouchScreenSeekPosition(String url, Object... objects);
}
