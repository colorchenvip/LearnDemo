package com.colorchen;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by color on 16/4/21 14:39.
 */
public class LearnDemoApp extends Application {

    private RefWatcher refWatcher;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        refWatcher = LeakCanary.install(this);
        setupRealm();
    }

    private void setupRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static RefWatcher getWatcher(Context context) {
        LearnDemoApp application = (LearnDemoApp) context.getApplicationContext();
        return application.refWatcher;
    }


}
