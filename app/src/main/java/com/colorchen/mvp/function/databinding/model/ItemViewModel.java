package com.colorchen.mvp.function.databinding.model;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

/**
 * ViewModel
 * Author ChenQ on 2017/7/10
 * email：wxchenq@yutong.com
 */
public class ItemViewModel {
    private final ObservableBoolean isVisibility = new ObservableBoolean(false);
    private final ObservableField<String> mImageUrl = new ObservableField<>();
    private final ObservableField<String> text = new ObservableField<>();


    public Context mContext;
    public String mUrl;


    public ItemViewModel(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(MeiZiModel.Result result,boolean isShowText){
        if(result == null ){
            return;
        }
        mImageUrl.set(result.getUrl());
        mUrl = result.getUrl();
        text.set(result.getDesc());
        isVisibility.set(isShowText);
    }

    public ObservableBoolean getIsVisibility() {
        return isVisibility;
    }
    public ObservableField<String> getImageUrl() {
        return mImageUrl;
    }
    public ObservableField<String> getText() {
        return text;
    }
}
