package com.colorchen.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.colorchen.App;
import com.colorchen.R;

import java.io.File;

/**
 * Created by color on 16/4/26 11:05.
 */
public class AccountManager {
    public static String getVersionName() {
        try {
            PackageManager manager = App.context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.context.getPackageName(), 0);
            String version = info.versionName;
            return App.context.getString(R.string.version) + version;
        } catch (Exception e) {
            e.printStackTrace();
            return App.context.getString(R.string.can_not_find_version_name);
        }
    }

    public static void openAppInfo(Context context) {
        //redirect user to app Settings
        Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getApplicationContext().getPackageName()));
        context.startActivity(i);
    }

    public static boolean clearCache() {
        //this method does not work on cacheDir
        // but works for fileDir, don't know why
        File cacheDir = App.context.getCacheDir();
        for (File file : cacheDir.listFiles()) {
            if (!file.delete()) {
                return false;
            }
        }
        return true;
    }

}
