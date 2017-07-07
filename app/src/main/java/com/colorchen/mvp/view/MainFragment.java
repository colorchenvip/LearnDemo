package com.colorchen.mvp.view;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.andview.refreshview.XRefreshView;
import com.colorchen.R;
import com.colorchen.mvp.view.recycleview.CustomerFooter;
import com.colorchen.mvp.view.recycleview.IndexPageAdapter;
import com.colorchen.ui.BaseActivity;
import com.colorchen.ui.BaseFragment;
import com.colorchen.ui.widget.AdHeader;
import com.colorchen.ui.widget.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主页
 *
 * @author ChenQ
 * @time 2017/6/23 17:12
 * @email：colorchenvip@163.com
 */
public class MainFragment extends BaseFragment {

    @Bind(R.id.mainGv)
    GridView mMainGv;
    @Bind(R.id.mainFragmentXrv)
    XRefreshView mMainFragmentXrv;

    private BaseActivity context;

    private List<String> strList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private BannerViewPager mLoopViewPager;
    private AdHeader headerView;
    private int[] mImageIds = new int[]{R.mipmap.test01, R.mipmap.test02,
            R.mipmap.test03};// 测试图片id

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
        for (int i = 0; i < 50; i++) {
            strList.add("数据" + i);
        }
        headerView = new AdHeader(getContext());
        mLoopViewPager = (BannerViewPager) headerView.findViewById(R.id.index_viewpager);
        initViewPager();
        mMainFragmentXrv.setPullLoadEnable(true);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, strList);
        mMainGv.setAdapter(adapter);
        mMainFragmentXrv.setPinnedTime(1000);
        // mMainFragmentXrv.setAutoLoadMore(false);
        mMainFragmentXrv.setCustomHeaderView(headerView);
        mMainFragmentXrv.setCustomFooterView(new CustomerFooter(getContext()));
        // mMainFragmentXrv.setCustomHeaderView(new XRefreshViewHeader(this));
        mMainFragmentXrv.setMoveForHorizontal(true);
//        mMainFragmentXrv.setPinnedContent(true);
        mMainFragmentXrv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMainFragmentXrv.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                final List<String> addList = new ArrayList<String>();
                for (int i = 0; i < 20; i++) {
                    addList.add("数据" + (i + strList.size()));
                }

                new Handler().postDelayed(new Runnable() {

                    @SuppressLint("NewApi")
                    @Override
                    public void run() {
                        if (strList.size() <= 70) {
                            if (Build.VERSION.SDK_INT >= 11) {
                                strList.addAll(addList);
                                adapter.addAll(addList);
                            }
                            mMainFragmentXrv.stopLoadMore();
                        } else {
                            mMainFragmentXrv.setLoadComplete(true);
                        }
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mMainFragmentXrv.startRefresh();
    }

    private void initViewPager() {
        IndexPageAdapter pageAdapter = new IndexPageAdapter(getContext(), mImageIds);
        mLoopViewPager.setAdapter(pageAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
