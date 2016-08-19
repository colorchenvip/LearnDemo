package com.colorchen.mvp.player;

/**
 * 播放器状态枚举
 * Created by color on 16/7/19 15:29.
 */
public enum PlayerState {
    IDLE, NORMAL, PREPARING, PLAYING,
    PLAYING_BUFFERING_START, PAUSE, AUTO_COMPLETE,
    ERROR, BACKUP_PLAYING_BUFFERING_STATE
}
