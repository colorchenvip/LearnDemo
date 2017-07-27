package com.colorchen.mvp.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colorchen.LearnDemoApp;
import com.colorchen.R;
import com.colorchen.utils.AccountManager;
import com.colorchen.utils.FileUtil;
import com.colorchen.utils.UI;

import java.io.File;
import java.util.List;

/**
 * 系统设置页面
 * Created by color on 16/4/25 20:20.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{
    public static final String CLEAR_CACHE = "clear_cache";
    public static final String FEED_BACK = "feedback";
    public static final String APP_VERSION = "check_version";
    public static final String ORIGINAL_SPLASH = "original_splash";
    public static final String WELCOME_SPLASH = "welcome_splash";
    public static final String SECRET_MODE = "secret_mode";
    private static final long DURATION = 300;

    private Preference clearCache;
    private Preference about;
    private Preference version;
    private Preference splash;
    private Preference welcome;
    private CheckBoxPreference enableSister;
    private View rootView;

    private long startTime;
    private boolean first = true;
    private int secretIndex;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
//        args.putInt(Constants.TYPE, type);
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == rootView) {
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        }
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        clearCache = findPreference(CLEAR_CACHE);
        about = findPreference(FEED_BACK);
        version = findPreference(APP_VERSION);
        splash = findPreference(ORIGINAL_SPLASH);
        welcome = findPreference(WELCOME_SPLASH);
        clearCache.setSummary(clearCache.getSummary()+" "+ getCacheSize());
        
        splash.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                secretStepOne();
                return true;
            }
        });

        version.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.i("test", secretIndex + ">>>>");
                UI.showSnackLong(rootView, R.string.secret_mode_opened);
                return true;
            }
        });

        clearCache.setOnPreferenceClickListener(this);
        about.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case CLEAR_CACHE:
                AccountManager.openAppInfo(getActivity());
                break;
            case FEED_BACK:
                Intent email = new Intent(Intent.ACTION_SENDTO);
                PackageManager packageManager = LearnDemoApp.context.getPackageManager();
                List activities = packageManager.queryIntentActivities(email,
                        PackageManager.MATCH_DEFAULT_ONLY);
                if (activities.size() > 0) {
                    email.setData(Uri.parse("colorchenvip@163.com"));
                    email.putExtra(Intent.EXTRA_SUBJECT, "Knowledge Feedback");
                    email.putExtra(Intent.EXTRA_TEXT, "Hi，");
                    startActivity(email);
                } else {
                    UI.showSnack(rootView, R.string.email_not_install);
                }
                break;
        }
        return true;
    }

    private void secretStepOne() {
        if (first){
            startTime = System.currentTimeMillis();
            first = false;
        }

        if (System.currentTimeMillis() -startTime< DURATION*(secretIndex +1)){
            if (first) {
                startTime = System.currentTimeMillis();
                first = false;
            }
            if (System.currentTimeMillis() - startTime < DURATION * (secretIndex + 1)) {
                if (secretIndex < 3) {
                    secretIndex++;
                }

            }
        }
    }

    private String getCacheSize(){
        File file = getActivity().getApplicationContext().getCacheDir();
        return FileUtil.getFileSize(file);
    }
}
