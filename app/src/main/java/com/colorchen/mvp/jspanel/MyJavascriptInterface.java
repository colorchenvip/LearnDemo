package com.colorchen.mvp.jspanel;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.colorchen.mvp.jspanel.interf.JavascriptCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * 结果回调中转站
 * Created by color on 16/4/26 14:22.
 */
public class MyJavascriptInterface {
    private static final String kTag = "WVJB";
    Map<String,JavascriptCallback> map = new HashMap<>();

    public void addCallback(String key,JavascriptCallback callback){
        map.put(key,callback);
    }

    @JavascriptInterface
    public void onResultForScript(String key,String value){
        Log.i(kTag,"onResultForScript:"+value);
        JavascriptCallback callback = map.remove(key);
        if (callback != null){
            callback.onReceiveValue(value);
        }
    }
}
