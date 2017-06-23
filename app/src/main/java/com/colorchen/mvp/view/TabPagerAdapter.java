package com.colorchen.mvp.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 底部tabBar adapter
 * Author ChenQ on 2017/6/23
 * email：wxchenq@yutong.com
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titles;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<Fragment> fragments, String[] titles) {
        this.fragments = fragments;
        this.titles.clear();
        for (String item : titles) {
            this.titles.add(item);
        }
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
