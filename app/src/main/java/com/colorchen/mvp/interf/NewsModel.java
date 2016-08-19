package com.colorchen.mvp.interf;


import com.colorchen.mvp.other.NewsDetail;
import com.colorchen.mvp.other.NewsItem;

/**
 * deals with the data work
 */
public interface NewsModel<I extends NewsItem, D extends NewsDetail> {

    void getNews(int type, OnLoadDataListener listener);

    void getNewsDetail(I newsItem, OnLoadDetailListener<D> listener);

}