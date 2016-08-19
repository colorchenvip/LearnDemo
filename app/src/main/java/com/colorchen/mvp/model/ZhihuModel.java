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
import com.colorchen.utils.DateUtil;
import com.colorchen.utils.SPUtil;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Date;

import io.realm.Realm;
import io.realm.Sort;
import okhttp3.Call;
import okhttp3.Response;

/**
 * deals with the zhihu news' data work
 */
public class ZhihuModel implements NewsModel<ZhihuStory, ZhihuDetail> {

    private BaseActivity mActivity;

    public ZhihuModel(BaseActivity activity) {
        mActivity = activity;
    }

    private String date;
    private long lastGetTime;
    public static final int GET_DURATION = 2000;
    private int type;

    @Override
    public void getNews(final int type, final OnLoadDataListener listener) {
        this.type = type;

        lastGetTime = System.currentTimeMillis();
        final Callback<String> callback = new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response) throws Exception {
                ZhihuJson zhihuJson = Json.parseZhihuNews(response.body().string());
                saveZhihu(zhihuJson);
                date = zhihuJson.getDate();
                if (type == API.TYPE_BEFORE) {
                    SPUtil.save(Constants.DATE, date);
                }
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    getData(this);
                    return;
                }
                listener.onFailure("load zhihu news failed");
            }

            @Override
            public void onResponse(String response) {
                listener.onSuccess();
            }

        };

        getData(callback);
    }

    private void saveZhihu(final ZhihuJson zhihuJson) {
        if (null != zhihuJson) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (type == API.TYPE_LATEST) {
                        realm.where(ZhihuTop.class).findAll().clear();
                    }
                    realm.copyToRealmOrUpdate(zhihuJson);
                    realm.where(ZhihuJson.class).findAllSorted(Constants.DATE, Sort.DESCENDING);
                }
            });
        }
    }

    private void getData(Callback callback) {
        if (type == API.TYPE_LATEST) {
            Net.get(API.NEWS_LATEST, callback, API.TAG_ZHIHU);

        } else if (type == API.TYPE_BEFORE) {
            date = SPUtil.get(Constants.DATE, DateUtil.parseStandardDate(new Date()));
            Net.get(API.NEWS_BEFORE + date, callback, API.TAG_ZHIHU);
        }
    }


    @Override
    public void getNewsDetail(final ZhihuStory newsItem, final OnLoadDetailListener<ZhihuDetail> listener) {

        requestData(newsItem, listener);
    }

    private void requestData(final ZhihuStory newsItem, final OnLoadDetailListener<ZhihuDetail> listener) {
        lastGetTime = System.currentTimeMillis();

        Callback<ZhihuDetail> callback = new Callback<ZhihuDetail>() {
            @Override
            public ZhihuDetail parseNetworkResponse(Response response) throws Exception {
                return Json.parseZhihuDetail(response.body().string());
            }

            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(API.BASE_URL + newsItem.getId(), this, API.TAG_ZHIHU);
                    return;
                }
                listener.onFailure("load zhihu detail failed", e);
            }

            @Override
            public void onResponse(ZhihuDetail response) {
                DB.saveOrUpdate(mActivity.mRealm, response);
                listener.onDetailSuccess(response);
            }

        };
        Net.get(API.BASE_URL + newsItem.getId(), callback, API.TAG_ZHIHU);
    }


}
