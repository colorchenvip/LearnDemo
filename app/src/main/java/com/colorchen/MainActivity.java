package com.colorchen;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.colorchen.mvp.player.VideoPlayerStandardActivity;
import com.colorchen.mvp.view.TabsFragment;
import com.colorchen.mvp.view.TestJSDemo;
import com.colorchen.ui.BaseActivity;
import com.colorchen.ui.SettingActivity;
import com.colorchen.utils.UI;

import butterknife.Bind;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    //    private static final String DEFAULT_TEST_URL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
//    private static final String DEFAULT_TEST_URL = "http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8";
    private static final String DEFAULT_TEST_URL = "http://7xs9fl.media1.z0.glb.clouddn.com/video/4cb0a21d15f53e212c905e309d49d069.mp4";
//    private static final String DEFAULT_TEST_URL = "http://7xs9fl.media1.z0.glb.clouddn.com/video/48a456c0f36f239716219d6c83ba3f1d.mp4";
//    private static final String DEFAULT_TEST_URL = "http://7xs9fl.media1.z0.glb.clouddn.com/video/057e5c365f677f91ef2aeddacb13866e.mp4";
//    private static final String DEFAULT_TEST_URL = "http://7xs9fl.media1.z0.glb.clouddn.com/video/dc668b81fc18d77139ddd85d63c03623.mp4";
//    private static final String DEFAULT_TEST_URL = "http://remoteconnector.eceibs20.com/test/course/LDOC002DEMO/content/56cea9da3a508.mp4";


    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();

        setupDrawer();
        setNavigationView();

        replace(TabsFragment.MENU_NEWS);
    }

    private void setNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            replaceFragment(TabsFragment.newInstance(type), type);
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
            replace(TabsFragment.MENU_NEWS);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, SettingActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Uri uri = Uri.parse(DEFAULT_TEST_URL);
            startActivity(new Intent(this, VideoPlayerStandardActivity.class)
                    .setData(uri)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (id == R.id.testJS) {
            startActivity(new Intent(this, TestJSDemo.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
