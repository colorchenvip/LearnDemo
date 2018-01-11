package com.colorchen.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * loading img encapsulation.
 */
public class Imager {

    public static void load(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .into(view);
    }

    public static void load(Context context, int resourceId, ImageView view) {
        Glide.with(context)
                .load(resourceId)
                .into(view);
    }

}
