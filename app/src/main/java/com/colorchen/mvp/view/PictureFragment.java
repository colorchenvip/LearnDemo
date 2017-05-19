package com.colorchen.mvp.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.colorchen.ui.BaseActivity;
import com.colorchen.utils.Constants;
import com.colorchen.utils.SPUtil;

/**
 * Created by color on 16/4/25 18:57.
 */
public class PictureFragment extends RecyclerFragment {

    private String url;
    private int page = 1;
    private StaggeredGridLayoutManager layoutManager;
    private long GET_DURATION = 3000;
    private LocalBroadcastManager localBroadcastManager;
    private BaseActivity context;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        lastPosition = SPUtil.getInt(type + Constants.POSITION);
        recyclerView.scrollToPosition(lastPosition > 0 ? lastPosition : 0);
    }


    @Override
    public void onPause() {
        firstPosition = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()])[0];
        super.onPause();
        showProgress(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static PictureFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, type);
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        context = (BaseActivity) getActivity();

    }

    @Override
    protected void AlwaysInit() {
        super.AlwaysInit();
    }

    @Override
    protected void initData() {

    }
    @Override
    public void onRefresh() {

    }

}
