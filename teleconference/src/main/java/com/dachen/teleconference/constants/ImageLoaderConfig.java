package com.dachen.teleconference.constants;

import android.graphics.Bitmap;

import com.dachen.teleconference.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @author gzhuo
 * @date 2016/8/19
 */
public class ImageLoaderConfig {
    public static DisplayImageOptions mCircleImageOptions = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .resetViewBeforeLoading(true)
            .displayer(new RoundedBitmapDisplayer(360))
            .showImageForEmptyUri(R.drawable.phone_meeting_default_head)
            .showImageOnFail(R.drawable.phone_meeting_default_head)
            .showImageOnLoading(R.drawable.phone_meeting_default_head)
            .build();
}
