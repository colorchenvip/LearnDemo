package com.colorchen.net.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.colorchen.R;
import com.colorchen.net.UrlUtils;
import com.colorchen.net.callback.DialogCallback;
import com.colorchen.net.callback.StringDialogCallback;
import com.colorchen.net.demo.base.NetBaseDetailActivity;
import com.colorchen.net.model.LzyResponse;
import com.colorchen.net.model.ServerModel;
import com.colorchen.net.utils.ColorUtils;
import com.lzy.okgo.OkGo;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 请求方法演示
 * Author ChenQ on 2017/5/19
 * email：wxchenq@yutong.com
 */
public class MethodActivity extends NetBaseDetailActivity implements AdapterView.OnItemClickListener{
    @Bind(R.id.gridView)
    GridView gridView;

    private String[] methods = {"GET", "HEAD\n只有请求头", "OPTIONS\n获取服务器支持的HTTP请求方式",//
            "POST", "PUT\n用法同POST主要用于创建资源", "DELETE\n与PUT对应主要用于删除资源"};

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.net_recycler_method);
        ButterKnife.bind(this);

        setTitle("请求方法演示");
        gridView.setAdapter(new MyAdapter());
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                OkGo.get(UrlUtils.URL_METHOD)//
                        .tag(this)//
                        .headers("header1", "headerValue1")//
                        .params("param1", "paramValue1")//
                        .execute(new DialogCallback<LzyResponse<ServerModel>>(this) {
                            @Override
                            public void onSuccess(LzyResponse<ServerModel> responseData, Call call, Response response) {
                                handleResponse(responseData.data, call, response);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                handleError(call, response);
                            }
                        });
                break;
            case 1:
                OkGo.head(UrlUtils.URL_METHOD)//
                        .tag(this)//
                        .headers("header1", "headerValue1")//
                        .params("param1", "paramValue1")//
                        .execute(new StringDialogCallback(this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                handleResponse(s, call, response);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                handleError(call, response);
                            }
                        });
                break;
            case 2:
                OkGo.options(UrlUtils.URL_METHOD)//
                        .tag(this)//
                        .headers("header1", "headerValue1")//
                        .params("param1", "paramValue1")//
                        .execute(new DialogCallback<LzyResponse<ServerModel>>(this) {
                            @Override
                            public void onSuccess(LzyResponse<ServerModel> responseData, Call call, Response response) {
                                handleResponse(responseData.data, call, response);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                handleError(call, response);
                            }
                        });
                break;
            case 3:
                OkGo.post(UrlUtils.URL_METHOD)//
                        .tag(this)//
                        .headers("header1", "headerValue1")//
                        .params("param1", "paramValue1")//
                        .execute(new DialogCallback<LzyResponse<ServerModel>>(this) {
                            @Override
                            public void onSuccess(LzyResponse<ServerModel> responseData, Call call, Response response) {
                                handleResponse(responseData.data, call, response);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                handleError(call, response);
                            }
                        });
                break;
            case 4:
                OkGo.put(UrlUtils.URL_METHOD)//
                        .tag(this)//
                        .headers("header1", "headerValue1")//
                        .params("param1", "paramValue1")//
                        .execute(new DialogCallback<LzyResponse<ServerModel>>(this) {
                            @Override
                            public void onSuccess(LzyResponse<ServerModel> responseData, Call call, Response response) {
                                handleResponse(responseData.data, call, response);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                handleError(call, response);
                            }
                        });
                break;
            case 5:
                OkGo.delete(UrlUtils.URL_METHOD)//
                        .tag(this)//
                        .headers("header1", "headerValue1")//
                        .params("param1", "paramValue1")//
                        .requestBody(RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), "这是要上传的数据"))//
                        .execute(new DialogCallback<LzyResponse<ServerModel>>(this) {
                            @Override
                            public void onSuccess(LzyResponse<ServerModel> responseData, Call call, Response response) {
                                handleResponse(responseData.data, call, response);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                handleError(call, response);
                            }
                        });
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return methods.length;
        }

        @Override
        public String getItem(int position) {
            return methods[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(getApplicationContext());
            }
            TextView textView = (TextView) convertView;
            textView.setGravity(Gravity.CENTER);
            textView.setHeight(200);
            textView.setText(getItem(position));
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(16);
            textView.setBackgroundColor(ColorUtils.randomColor());
            return textView;
        }
    }
}
