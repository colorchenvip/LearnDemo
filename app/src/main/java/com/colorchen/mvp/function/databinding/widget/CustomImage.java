package com.colorchen.mvp.function.databinding.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 使用Glide 加载图片
 * Author ChenQ on 2017/7/10
 * email：wxchenq@yutong.com
 */
public class CustomImage extends ImageView{
    public CustomImage(Context context) {
        super(context);
    }

    public CustomImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void load(String url){
        Glide.with(this.getContext().getApplicationContext()).load(url).fitCenter().into(this);
    }
}
