package com.colorchen.mvp.interf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.colorchen.mvp.preseter.FetchService;

/**
 * Created by color on 16/4/26 16:43.
 */
public class UpdateReceiver extends BroadcastReceiver {
    private OnLoadDataListener loadDataListener;
    public UpdateReceiver(OnLoadDataListener loadDataListener){
        this.loadDataListener = loadDataListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra(FetchService.EXTRA_FETCHED_RESULT, false)) {
            loadDataListener.onSuccess();
        } else {
            loadDataListener.onFailure("load no results");
        }
                
    }
}
