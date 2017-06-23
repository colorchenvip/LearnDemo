package com.colorchen.mvp.view;

import android.os.Bundle;

import com.colorchen.R;
import com.colorchen.ui.BaseFragment;

/**
 * 我
 * @author ChenQ
 * @time 2017/6/23 17:14
 * @emachenvip@163.com
 */
public class Tab1Fragment extends BaseFragment {

    public static Tab1Fragment newInstance() {
        Bundle args = new Bundle();
//        args.putInt(Constants.TYPE, type);
        Tab1Fragment fragment = new Tab1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_content_tab;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
