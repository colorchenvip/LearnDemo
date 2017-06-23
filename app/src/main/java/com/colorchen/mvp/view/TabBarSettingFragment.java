package com.colorchen.mvp.view;

import android.os.Bundle;

import com.colorchen.R;
import com.colorchen.ui.BaseFragment;

/**
 * tabBar 控件的设置
 * @author ChenQ
 * @time 2017/6/23 17:14
 * @email：colorchenvi3.com
 */
public class TabBarSettingFragment extends BaseFragment {

    public static TabBarSettingFragment newInstance() {
        Bundle args = new Bundle();
//        args.putInt(Constants.TYPE, type);
        TabBarSettingFragment fragment = new TabBarSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
            layoutId = R.layout.fragment_tabbar_setting;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
