package com.colorchen.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.colorchen.LearnDemoApp;

/**
 * loading img encapsulation.
 */
public class Imager {

    public static void load(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(view);
    }

    public static void load(Context context, int resourceId, ImageView view) {
        Glide.with(context)
                .load(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(view);
    }

    public static void loadWithHighPriority(String url, ImageView view) {
        Glide.with(LearnDemoApp.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(view);
    }

    public static void load(String url, int animationId, ImageView view) {
        Glide.with(LearnDemoApp.context)
                .load(url)
                .animate(animationId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

}
