package com.colorchen;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.colorchen.mvp.player.VideoPlayerStandardActivity;
import com.colorchen.mvp.view.MainFragment;
import com.colorchen.mvp.view.MeFragment;
import com.colorchen.mvp.view.Tab1Fragment;
import com.colorchen.mvp.view.TabBarSettingFragment;
import com.colorchen.mvp.view.TabPagerAdapter;
import com.colorchen.mvp.view.TabsFragment;
import com.colorchen.mvp.view.TabsFragmentBottom;
import com.colorchen.mvp.view.TestJSDemo;
import com.colorchen.net.OkHttpMainActivity;
import com.colorchen.ui.BaseActivity;
import com.colorchen.ui.SettingActivity;
import com.colorchen.ui.utils.StateBarTranslucentUtils;
import com.colorchen.utils.UI;
import com.jpeng.jptabbar.BadgeDismissListener;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.colorchen.LearnDemoApp.context;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BadgeDismissListener, OnTabSelectListener {

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Titles
    private static final String[] mTitles = {"页面一", "页面二", "页面三", "页面四"};
    @SeleIcons
    private static final int[] mSeleIcons = {R.mipmap.tab1_selected, R.mipmap.tab2_selected, R.mipmap.tab3_selected, R.mipmap.tab4_selected};

    @NorIcons
    private static final int[] mNormalIcons = {R.mipmap.tab1_normal, R.mipmap.tab2_normal, R.mipmap.tab3_normal, R.mipmap.tab4_normal};
    /*页面的数量*/
    private List<Fragment> fragments = new ArrayList<>();

    private TabPagerAdapter adapter;
    @Bind(R.id.viewPagerBottom)
    ViewPager pager;
    @Bind(R.id.tabBarBottom)
    JPTabBar mTabBar;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_main;
    }

    @Override
    protected void initViews() {
//        super.initViews();
        setupDrawer();
        setNavigationView();
        initTabBar();
    }

    private void initTabBar() {
        adapter = new TabPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        mTabBar.setTabListener(this);
        mTabBar.setContainer(pager);
        mTabBar.setDismissListener(this);
        //显示圆点模式的徽章
        //设置容器
        mTabBar.showBadge(0, 50);
        //设置Badge消失的代理
        mTabBar.setTabListener(this);
        fragments.add(MainFragment.newInstance());
        fragments.add(TabBarSettingFragment.newInstance());
        fragments.add(MeFragment.newInstance());
        fragments.add(Tab1Fragment.newInstance());
        adapter.setFragments(fragments,mTitles);
    }
    @Override
    public void onDismiss(int position) {
        if (position == 0) {
//            mTab1.clearCount();
        }
    }

    @Override
    public void onTabSelect(int index) {

    }

    private void setNavigationView() {
        //禁止右划推书功能
        setSwipeBackEnable(false);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //DrawerLayout兼容4.4
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //将侧边栏顶部延伸至status bar
            drawer.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            drawer.setClipToPadding(false);
        }
        //设置状态栏透明
        StateBarTranslucentUtils.setStateBarTranslucent(this);
        //获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int stateBarHeight =  getResources().getDimensionPixelSize(resourceId);
        //设置margin
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setMargins(0, stateBarHeight, 0, 0);
        toolbar.setLayoutParams(layoutParams);
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawerLayout != null) {
            drawerLayout.setDrawerListener(toggle);
        }
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            doublePressBackToQuit();
        }
    }

    boolean backPressed;

    private void doublePressBackToQuit() {
        if (backPressed) {
            super.onBackPressed();
            return;
        }
        backPressed = true;
        UI.showSnack(drawerLayout, R.string.leave_app);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_about) {
            Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String currentType;

    public void replace(String type) {
        if (!type.equals(currentType)) {
            currentType = type;
            replaceFragment(TabsFragmentBottom.newInstance(type), type);
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        supportPostponeEnterTransition();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Snackbar.make(this.getCurrentFocus(), "相机", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (id == R.id.nav_gallery) {
            replace(TabsFragment.MENU_MAIN);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, SettingActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_net) {
            //网络组建
            startActivity(new Intent(getApplicationContext(), OkHttpMainActivity.class));

        } else if (id == R.id.nav_video) {
            //视频播放
            startActivity(new Intent(this, VideoPlayerStandardActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (id == R.id.testJS) {
            startActivity(new Intent(this, TestJSDemo.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
