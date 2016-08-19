package com.colorchen;

import android.support.multidex.MultiDexApplication;

/**
 * Created by color on 16/8/18 17:43.
 */

public class Application extends MultiDexApplication {
    private static Application self;

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
    }

    public static Application getSelf() {
        return self;
    }
}
