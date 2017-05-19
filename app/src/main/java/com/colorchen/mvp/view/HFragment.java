package com.colorchen.mvp.view;

import android.os.Bundle;

import com.colorchen.utils.Constants;

/**
 * Created by color on 16/4/26 17:00.
 */
public class HFragment extends RecyclerFragment {

    public static HFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, type);
        HFragment fragment = new HFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    protected void initData() {

    }
}
