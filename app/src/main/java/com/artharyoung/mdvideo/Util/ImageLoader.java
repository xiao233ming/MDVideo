package com.artharyoung.mdvideo.Util;

import android.content.Context;
import android.widget.ImageView;

import com.artharyoung.mdvideo.R;
import com.bumptech.glide.Glide;

/**
 * Created by syamiadmin on 2016/6/8.
 */
public class ImageLoader {
    public static void LoadNormalImage(Context context, String url, ImageView imageView){
        Glide.with(context).load(url)
                .placeholder(R.drawable.empty_photo)
                .crossFade()
                .into(imageView);
    }
}
