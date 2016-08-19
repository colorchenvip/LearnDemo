package com.colorchen.mvp.player.base;

import java.util.Map;

/**
 * Created by color on 16/7/18 16:48.
 */
public class PathBean {
    public String url;
    public Map<String, String> mapHeadData;
    public boolean looping;

    public PathBean(String url, Map<String, String> mapHeadData, boolean loop) {
        this.url = url;
        this.mapHeadData = mapHeadData;
        this.looping = loop;
    }
}
