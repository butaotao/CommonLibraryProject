package com.dachen.teleconference.constants;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dachen.teleconference.R;

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
            .showImageForEmptyUri(R.drawable.ic_default_head)
            .showImageOnFail(R.drawable.ic_default_head)
            .showImageOnLoading(R.drawable.ic_default_head)
            .displayer(new RoundedBitmapDisplayer(360))
            .build();
}
