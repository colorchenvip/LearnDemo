package com.colorchen.mvp.function.databinding.widget;

import android.databinding.BindingAdapter;

/**
 * Author ChenQ on 2017/7/10
 * email：wxchenq@yutong.com
 */
public class CustomImageAdapter {
    @BindingAdapter("load")
    public static void load(CustomImage imageView,String url){
        imageView.load(url);
    }
}
