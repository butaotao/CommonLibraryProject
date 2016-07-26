package com.dachen.common;

import android.content.Context;

import com.dachen.common.toolbox.CommonManager;
import com.dachen.common.toolbox.OnCommonRequestListener;

/**
 * Created by Mcp on 2016/4/25.
 */
public class DCommonSdk {
    public static Context mContext;

    public static void setCommonRequestListener(OnCommonRequestListener l){
        CommonManager.setCommonRequestListener(l);
    }
}
