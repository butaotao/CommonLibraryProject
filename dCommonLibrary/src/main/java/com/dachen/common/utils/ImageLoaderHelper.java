package com.dachen.common.utils;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * ImageLoader工具类
 * Created by TianWei on 2016/7/14.
 */
public class ImageLoaderHelper {
    private static ImageLoaderHelper INSTANCE;

    private ImageLoaderHelper() {
    }

    public static ImageLoaderHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (ImageLoaderHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ImageLoaderHelper();
                }
            }
        }
        return INSTANCE;
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener,
                             boolean small) {
        if (small) {
            if (null != uri && uri.startsWith("http")) {
                uri += "-small1";
                ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
            } else {
                ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
            }
        } else {
            ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
        }
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, boolean small) {
        if (small) {
            if (null != uri && uri.startsWith("http")) {
                uri += "-small1";
                ImageLoader.getInstance().displayImage(uri, imageView, options);
            } else {
                ImageLoader.getInstance().displayImage(uri, imageView, options);
            }
        } else {
            ImageLoader.getInstance().displayImage(uri, imageView, options);
        }
    }

    public void displayImage(String uri, ImageView imageView, ImageLoadingListener listener, boolean small) {
        if (small) {
            if (null != uri && uri.startsWith("http")) {
                uri += "-small1";
                ImageLoader.getInstance().displayImage(uri, imageView, listener);
            } else {
                ImageLoader.getInstance().displayImage(uri, imageView, listener);
            }
        } else {
            ImageLoader.getInstance().displayImage(uri, imageView, listener);
        }
    }

    public void displayImage(String uri, ImageView imageView, boolean small) {
        if (small) {
            if (null != uri && uri.startsWith("http")) {
                uri += "-small1";
                ImageLoader.getInstance().displayImage(uri, imageView);
            } else {
                ImageLoader.getInstance().displayImage(uri, imageView);
            }
        } else {
            ImageLoader.getInstance().displayImage(uri, imageView);
        }
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    public void displayImage(String uri, ImageView imageView, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageView, listener);
    }

    public void displayImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView);
    }
}
