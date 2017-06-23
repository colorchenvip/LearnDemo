package com.colorchen.mvp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.colorchen.R;
import com.colorchen.ui.BaseFragment;
import com.colorchen.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部的tab管理manager
 * Created by color on 16/4/25 14:53.
 */
public class TabsFragmentBottom extends BaseFragment {
    public static final String MENU_MAIN = "main";
    public static final String MENU_ME = "me";
    public static final String MENU_SETTING = "setting";
    public static final String MENU_TAB1 = "setting";
    public static final String MENU_TAB2 = "setting";
    private static final int SMOOTHSCROLL_TOP_POSITION = 50;

    /*菜单的分类*/
    private String menuType;
    /*页面的数量*/
    private List<Fragment> fragments = new ArrayList<>();


    public static TabsFragmentBottom newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        TabsFragmentBottom tabsFragment = new TabsFragmentBottom();
        tabsFragment.setArguments(bundle);
        return tabsFragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_tab_top;
    }

    @Override
    protected void initViews() {


    }

    @Override
    protected void initData() {
        menuType = getArguments().getString(Constants.TYPE,MENU_MAIN);
        if (menuType.equals(MENU_MAIN)) {

        }

    }

    private void scrollToTop(RecyclerView list) {
        if (null != list) {
            int lastPosition;
            if (MENU_ME.equals(menuType)) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) list.getLayoutManager();
                lastPosition = manager.findLastVisibleItemPositions(
                        new int[manager.getSpanCount()])[1];

            } else {
                LinearLayoutManager manager = (LinearLayoutManager) list.getLayoutManager();
                lastPosition = manager.findLastVisibleItemPosition();
            }
            if (lastPosition < SMOOTHSCROLL_TOP_POSITION) {
                list.smoothScrollToPosition(0);
            } else {
                list.scrollToPosition(0);
            }
        }
    }

}
