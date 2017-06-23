package com.colorchen.mvp.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.colorchen.R;
import com.colorchen.ui.BaseActivity;
import com.colorchen.ui.BaseFragment;

/**
 * 主页
 * @author ChenQ
 * @time 2017/6/23 17:12
 * @email：colorchenvip@163.com
 */
public class MainFragment extends BaseFragment {

    private BaseActivity context;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
//        args.putInt(Constants.TYPE, type);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_content_main;
    }

    @Override
    protected void initViews() {
        context = (BaseActivity) getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
