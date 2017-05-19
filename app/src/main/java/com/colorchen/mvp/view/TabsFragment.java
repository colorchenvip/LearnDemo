package com.colorchen.mvp.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.colorchen.R;
import com.colorchen.ui.BaseFragment;
import com.colorchen.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by color on 16/4/25 14:53.
 */
public class TabsFragment extends BaseFragment {
    public static final String MENU_NEWS = "news";
    public static final String MENU_PIC = "pic";
    public static final String MENU_SECRET = "secret";

    private static final int SMOOTHSCROLL_TOP_POSITION = 50;

    private TabPagerAdapter adapter;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout tabs;

    /*菜单的分类*/
    String menuType;
    /*页面的数量*/
    private List<RecyclerFragment> fragments = new ArrayList<>();

    public static TabsFragment newInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE,type);
        TabsFragment tabsFragment = new TabsFragment();
        tabsFragment.setArguments(bundle);
        return tabsFragment;
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initData() {

    }



    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_tab;
    }


    private void scrollToTop(RecyclerView list){
        if (null != list) {
            int lastPosition;
            if (MENU_PIC.equals(menuType)) {
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


    public static class TabPagerAdapter extends FragmentPagerAdapter{

        private List<RecyclerFragment> fragments;
        private List<String> titles;

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setFragments(List<RecyclerFragment> fragments, List<String> titles) {
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
