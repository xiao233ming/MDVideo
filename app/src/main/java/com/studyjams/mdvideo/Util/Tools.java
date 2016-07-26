package com.studyjams.mdvideo.Util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.studyjams.mdvideo.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by syamiadmin on 2016/6/8.
 */
public class Tools {

    /**图片加载**/
    public static void LoadNormalImage(Context context, String url, ImageView imageView){
        Glide.with(context).load(url)
                .placeholder(R.mipmap.empty_photo)
                .crossFade()
                .into(imageView);
    }

    /**获取当前本地系统时间的**/
    public static String getCurrentTimeMillis() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
}
