package com.colorchen.mvp.model;


import com.colorchen.mvp.interf.NewsModel;
import com.colorchen.mvp.interf.OnLoadDataListener;
import com.colorchen.mvp.interf.OnLoadDetailListener;
import com.colorchen.mvp.net.API;
import com.colorchen.mvp.net.DB;
import com.colorchen.mvp.net.Json;
import com.colorchen.mvp.net.Net;
import com.colorchen.ui.BaseActivity;
import com.colorchen.utils.Constants;
import com.colorchen.utils.SPUtil;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Response;

/**
 * deals with the fresh news' data work
 */
public class FreshModel implements NewsModel<FreshPost, FreshDetailJson> {
    /**
     * clear page record to zero and start new request
     */
    public static final int TYPE_FRESH = 0;
    /**
     * a continuous request with increasing one page each time
     */
    public static final int TYPE_CONTINUOUS = 1;
    private final Realm realm;

    private int page;
    private long lastGetTime;
    public static final int GET_DURATION = 3000;
    private BaseActivity mActivity;

    public FreshModel(BaseActivity activity) {
        mActivity = activity;
        realm=mActivity.mRealm;
    }

    @Override
    public void getNews(int type, final OnLoadDataListener listener) {

        lastGetTime = System.currentTimeMillis();
        if (type == TYPE_FRESH) {
            page = 1;//如果是全新请求，就初始化page为1
        }
        getFreshNews(listener);
    }

    private void getFreshNews(final OnLoadDataListener listener) {
        Callback<FreshJson> callback = new Callback<FreshJson>() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(API.FRESH_NEWS + page, this, API.TAG_FRESH);
                    return;
                }
                listener.onFailure("load fresh news failed");
            }

            @Override
            public void onResponse(FreshJson response) {
                DB.saveList(realm, response.getPosts());
                listener.onSuccess();
                page++;
                SPUtil.save(Constants.PAGE, page);
            }

            @Override
            public FreshJson parseNetworkResponse(Response response) throws IOException {
                return Json.parseFreshNews(response.body().string());
            }
        };

        Net.get(API.FRESH_NEWS + page, callback, API.TAG_FRESH);
    }

    @Override
    public void getNewsDetail(final FreshPost freshPost, final OnLoadDetailListener<FreshDetailJson> listener) {
        if (getDetailFromDB(freshPost, listener)) return;

        requestData(freshPost, listener);
    }

    private boolean getDetailFromDB(FreshPost freshPost, OnLoadDetailListener<FreshDetailJson> listener) {
        FreshDetail post = DB.getById(realm, freshPost.getId(), FreshDetail.class);
        if (null != post) {
            FreshDetailJson detailNews = new FreshDetailJson();
            detailNews.setPost(post);
            listener.onDetailSuccess(detailNews);
            return true;
        }
        return false;
    }

    private void requestData(final FreshPost freshPost, final OnLoadDetailListener<FreshDetailJson> listener) {
        lastGetTime = System.currentTimeMillis();
        Callback<FreshDetailJson> callback = new Callback<FreshDetailJson>() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(API.FRESH_NEWS_DETAIL + freshPost.getId(), this, API.TAG_FRESH);
                    return;
                }
                listener.onFailure("load fresh detail failed", e);

            }

            @Override
            public FreshDetailJson parseNetworkResponse(Response response) throws IOException {
                return Json.parseFreshDetail(response.body().string());
            }

            @Override
            public void onResponse(FreshDetailJson response) {
                DB.save(realm, response.getPost());
                listener.onDetailSuccess(response);
            }
        };
        Net.get(API.FRESH_NEWS_DETAIL + freshPost.getId(), callback, API.TAG_FRESH);
    }
}
