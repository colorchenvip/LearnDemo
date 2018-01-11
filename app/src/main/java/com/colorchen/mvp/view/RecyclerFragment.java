package com.colorchen.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.colorchen.R;
import com.colorchen.ui.BaseFragment;
import com.colorchen.utils.Constants;
import com.colorchen.utils.SPUtil;

import butterknife.BindView;

/**
 * Created by color on 16/4/25 16:07.
 */
public abstract class RecyclerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    int type;
    int lastPosition;
    int firstPosition;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_recyler;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null){
            lastPosition = SPUtil.getInt(type+ Constants.POSITION);
            if (lastPosition>0){
                recyclerView.scrollToPosition(lastPosition);
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        SPUtil.save(type+Constants.POSITION,firstPosition);
    }

    @Override
    protected void initViews() {
        recyclerView.setHasFixedSize(true);
        swipeRefresh.setColorSchemeColors(R.color.colorPrimary,R.color.colorPrimaryDark,R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(this);
    }

    public RecyclerView getRecyclerView(){
        return new RecyclerView(getContext());
    }

    public void showProgress(final boolean refreshState){
        if (null != swipeRefresh){
            swipeRefresh.setRefreshing(refreshState);
        }
    }
}
