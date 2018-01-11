package com.colorchen.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.colorchen.App;

/**
 * Created by color on 16/4/25 18:16.
 */
public class SPUtil {
    private static Context context = App.context;
    private static SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    private static SharedPreferences.Editor editor = sp.edit();

    public static void save(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }
    public static void save(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static void save(String key, int value) {
        editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static String get(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public static String getString(String key) {
        return sp.getString(key, "");
    }
    public static int getInt(String key) {
        return sp.getInt(key, 0);
    }
    public static boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }
}
