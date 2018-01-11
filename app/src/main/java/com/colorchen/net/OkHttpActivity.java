package com.colorchen.net;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colorchen.R;
import com.colorchen.net.base.BaseRecyclerAdapter;
import com.colorchen.net.base.OkBaseActivity;
import com.colorchen.net.demo.MethodActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * des：OkHttp 使用的介绍
 *
 * @author ChenQ
 * @date 2017-12-4
 * email：wxchenq@yutong.com
 */
public class OkHttpActivity extends OkBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<String[]> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_recycler_okgo);
        initToolBar(toolbar, true, "OkGo功能介绍");

        initData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new MainAdapter(this));
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new String[]{"请求方法演示", "目前支持 GET，HEAD，OPTIONS，POST，PUT，DELETE"});
        data.add(new String[]{"请求图片", "请求服务器返回bitmap对象"});
        data.add(new String[]{"网络缓存基本用法", "默认提供了四种缓存模式，根据需要选择使用"});
        data.add(new String[]{"支持https请求", "支持 cer,bks 证书，支持双向认证"});
        data.add(new String[]{"同步请求", "允许直接返回Response对象，会阻塞主线程，需要自行开启子线程"});
        data.add(new String[]{"301重定向", "支持301重定向请求"});
        data.add(new String[]{"测试页面", "用于测试特殊情况下的网络连接,可忽略"});
    }

    private class MainAdapter extends BaseRecyclerAdapter<String[], ViewHolder> {

        public MainAdapter(Context context) {
            super(context, data);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.net_item_main_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String[] strings = mDatas.get(position);
            holder.bind(position, strings);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.des)
        TextView des;

        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position, String[] strings) {
            this.position = position;
            title.setText(strings[0]);
            des.setText(strings[1]);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*if (position == 0) startActivity(new Intent(OkHttpActivity.this, MethodActivity.class));
            if (position == 1) startActivity(new Intent(OkHttpActivity.this, BitmapRequestActivity.class));
            if (position == 2) startActivity(new Intent(OkHttpActivity.this, CacheActivity.class));
            if (position == 3) startActivity(new Intent(OkHttpActivity.this, HttpsActivity.class));
            if (position == 4) startActivity(new Intent(OkHttpActivity.this, SyncActivity.class));
            if (position == 5) startActivity(new Intent(OkHttpActivity.this, RedirectActivity.class));
            if (position == 6) startActivity(new Intent(OkHttpActivity.this, TestActivity.class));*/
            if (position == 0) {
                startActivity(new Intent(OkHttpActivity.this, MethodActivity.class));
            }
            if (position == 1) {
                showToast("002");
            }
            if (position == 2) {
                showToast("003");
            }
            if (position == 3) {
                showToast("004");
            }
            if (position == 4) {
                showToast("005");
            }
            if (position == 5) {
                showToast("006");
            }
            if (position == 6) {
                showToast("007");
            }
        }
    }
}

