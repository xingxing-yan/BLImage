package com.yyx.beautifylib.tag.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

/**
 * author: shell
 * date 2016/12/30 下午3:45
 **/
public class ImageLoader {

    public static void loadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .dontAnimate()
                .into(imageView);
    }

    public static void loadImage(Context context, String url, Target<Bitmap> target){
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(target);
    }
}
