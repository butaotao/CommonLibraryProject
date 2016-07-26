package com.dachen.common.toolbox;


/**
 * Created by Mcp on 2016/4/25.
 */
public class CommonManager {
    public static OnCommonRequestListener commonRequestListener;

    public static void setCommonRequestListener(OnCommonRequestListener l){
//        imSdkListener =new WeakReference<OnImSdkListener>(l);
        commonRequestListener = l;
    }

    public static boolean onRequestTokenErr(){
        if(commonRequestListener==null)return false;
        return commonRequestListener.onTokenErr();
    }
    public static boolean onUpdateVersionErr(String msg){
        if(commonRequestListener==null)return false;
        return commonRequestListener.onUpdateVersionErr(msg);
    }
}
