package com.colorchen.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import butterknife.ButterKnife;

/**
 * 兼容fragment 和 activity类型的基类
 * Author ChenQ on 2017/6/23
 * email：wxchenq@yutong.com
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
    protected View rootView;
    protected int layoutId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayoutId();
        setContentView(layoutId);
        AlwaysInit();
        initViews();
        initData();
    }

    protected void AlwaysInit() {
        ButterKnife.bind(this, rootView);
    }

    protected abstract void initLayoutId();

    protected abstract void initViews();

    protected abstract void initData();
}
