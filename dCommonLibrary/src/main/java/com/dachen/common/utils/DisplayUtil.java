package com.dachen.common.utils;

import android.content.Context;

import com.dachen.common.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class DisplayUtil {
    public static final int voice_view_max_width = 165;// dp
    public static final int voice_view_min_width = 30;// dp
    public static final float voice_max_length = 30;// 声音最长可以表现为多少毫秒（实际本程序是60s,但是如果这里是60s的话，当时间很短，就没啥差别

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getVoiceViewWidth(Context context, int seconds) {
        if (seconds >= voice_max_length) {
            return dip2px(context, voice_view_max_width);
        }
        final int dpLen = (int) ((seconds / voice_max_length) * (voice_view_max_width - voice_view_min_width)) + voice_view_min_width;
        return dip2px(context, dpLen);
    }

    public static DisplayImageOptions getHeadOptions() {
        return getOptions(R.drawable.ic_default_head);
    }

    public static DisplayImageOptions getOptions() {
        return getOptions(R.drawable.no_images);
    }

    public static DisplayImageOptions getOptions(int resId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(resId)
                .showImageOnFail(resId).displayer(new RoundedBitmapDisplayer(6)).cacheInMemory(true).cacheOnDisc(true)
                .build();
        return options;
    }

}
