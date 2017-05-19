package com.colorchen;

import android.app.Application;
import android.content.Context;


/**
 * Created by color on 16/4/21 14:39.
 */
public class LearnDemoApp extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
