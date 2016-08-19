package com.colorchen.mvp.player.interf;

/**
 * 横屏播放器的点击接口
 * 可以自定义一下回调事件
 * Created by color on 16/7/20 10:26.
 */
public interface MediaBuriedPointStandard extends MediaBuriedPoint{
    void onClickStartThumb(String url, Object... objects);

    void onClickBlank(String url, Object... objects);

    void onClickBlankFullscreen(String url, Object... objects);

    abstract class Stub implements MediaBuriedPointStandard{
        @Override
        public void onClickStartThumb(String url, Object... objects) {

        }

        @Override
        public void onClickBlank(String url, Object... objects) {

        }

        @Override
        public void onClickBlankFullscreen(String url, Object... objects) {

        }

        @Override
        public void onClickStartIcon(String url, Object... objects) {

        }

        @Override
        public void onClickStartError(String url, Object... objects) {

        }

        @Override
        public void onClickStop(String url, Object... objects) {

        }

        @Override
        public void onClickStopFullscreen(String url, Object... objects) {

        }

        @Override
        public void onClickResume(String url, Object... objects) {

        }

        @Override
        public void onClickResumeFullscreen(String url, Object... objects) {

        }

        @Override
        public void onClickSeekBar(String url, Object... objects) {

        }

        @Override
        public void onClickSeekBarFullscreen(String url, Object... objects) {

        }

        @Override
        public void onAutoComplete(String url, Object... objects) {

        }

        @Override
        public void onAutoCompleteFullscreen(String url, Object... objects) {

        }

        @Override
        public void onEnterFullscreen(String url, Object... objects) {

        }

        @Override
        public void onQuitFullscreen(String url, Object... objects) {

        }

        @Override
        public void onTouchScreenSeekVolume(String url, Object... objects) {

        }

        @Override
        public void onTouchScreenSeekPosition(String url, Object... objects) {

        }
    }
}
