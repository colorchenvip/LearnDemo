package com.colorchen.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.colorchen.R;
import com.colorchen.ui.utils.StateBarTranslucentUtils;

import butterknife.ButterKnife;
import io.realm.Realm;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * 基类
 * Created by color on 16/4/13 17:20.
 */
public abstract class BaseActivity extends SwipeBackActivity {

    protected int layoutId = R.layout.activity_base;
    protected Toolbar toolbar;
    private boolean isShowToolbar = true;
    public Realm mRealm;
    // 右滑返回
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initLayoutId();
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        setContentView(layoutId);
        ButterKnife.bind(this);
        initAppBar();
        initViews();
        initData();
    }

    protected void onCreate() {

    }

    protected void initViews() {
        //设置状态栏透明
        StateBarTranslucentUtils.setStateBarTranslucent(this);
        //状态栏着色
        StateBarTranslucentUtils.setStateBarColor(this);

    }
    protected void initData() {

    }

    private void initAppBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != toolbar) {
            setSupportActionBar(toolbar);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fragment, tag);
        fragmentTransaction.commit();
    }

    public void toggleToolbar() {
        if (isShowToolbar) {
            hideToolbar();
        } else {
            showToolbar();
        }
    }

    public void hideToolbar() {
        if (toolbar != null) {
            isShowToolbar = false;
            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        }
    }

    public void showToolbar() {
        if (toolbar != null) {
            isShowToolbar = true;
            toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        }
    }

    protected abstract void initLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
