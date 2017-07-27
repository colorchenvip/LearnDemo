package com.colorchen.mvp.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.andview.refreshview.XRefreshView;
import com.colorchen.R;
import com.colorchen.mvp.function.databinding.DataBindingActivity;
import com.colorchen.mvp.function.earth.BaiDuEarthActivity;
import com.colorchen.mvp.player.VideoPlayerStandardActivity;
import com.colorchen.mvp.view.recycleview.CustomerFooter;
import com.colorchen.mvp.view.recycleview.IndexPageAdapter;
import com.colorchen.net.OkHttpMainActivity;
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

    private List<String> strList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private BannerViewPager mLoopViewPager;
    private AdHeader headerView;
    private int[] mImageIds = new int[]{R.mipmap.test01, R.mipmap.test02, R.mipmap.test03};// 测试图片id

    private int index = 1;//数据的起点位置

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
        strList.add(index + " 列表的相关使用");
        strList.add(++index + " 自定义小视频");
        strList.add(++index + " okHttp相关使用");
        strList.add(++index + " dataBinding 案例");
        strList.add(++index + " 地图使用案例");
        index++;
        for (int i = index; i < 50; i++) {
            strList.add("数据" + i);
        }
        initBanner();
        initGridView();
        initRefreshView();

    }

    @Override
    protected void initData() {
        mMainFragmentXrv.startRefresh();
    }

    private void initGridView() {
         /*刷新组件的相关设置*/
        mMainFragmentXrv.setPullLoadEnable(true);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strList);
        mMainGv.setAdapter(adapter);
        mMainGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        //视频播放
                        startActivity(new Intent(context, VideoPlayerStandardActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                    case 2:
                        //网络组建
                        startActivity(new Intent(context, OkHttpMainActivity.class));
                        break;
                    case 3:
                        //dataBinding use
                        startActivity(new Intent(context, DataBindingActivity.class));
                        break;
                    case 4:
                        // earth use 地图使用
                        startActivity(new Intent(context, BaiDuEarthActivity.class));
                        break;
                     default:
                        break;

                }
            }
        });
    }

    private void initRefreshView() {
        mMainFragmentXrv.setPinnedTime(1000);
        // mMainFragmentXrv.setAutoLoadMore(false);
        mMainFragmentXrv.setCustomHeaderView(headerView);
        mMainFragmentXrv.setCustomFooterView(new CustomerFooter(context));
//        mMainFragmentXrv.setCustomHeaderView(new XRefreshViewHeader(context));
        mMainFragmentXrv.setMoveForHorizontal(true);
//        mMainFragmentXrv.setPinnedContent(true);
        mMainFragmentXrv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mMainFragmentXrv != null) {
                            mMainFragmentXrv.stopRefresh();
                        }
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

    private void initBanner() {
        //初始化banner
        headerView = new AdHeader(getContext());
        mLoopViewPager = (BannerViewPager) headerView.findViewById(R.id.index_viewpager);
        IndexPageAdapter pageAdapter = new IndexPageAdapter(getContext(), mImageIds);
        mLoopViewPager.setAdapter(pageAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
