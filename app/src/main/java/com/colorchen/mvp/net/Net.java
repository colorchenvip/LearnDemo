package com.colorchen.mvp.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * Net request encapsulation
 */
public class Net {

    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static void get(String url, Callback callback, Object tag) {
        OkHttpUtils.get().url(url).tag(tag)
                .build().execute(callback);
    }

}
