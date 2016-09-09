package com.dachen.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.List;

/**
 * Created by Mcp on 2016/7/14.
 */
public class CommonUtils {
    public static String getProcessName(Context context){
        String name="";
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        int myPid =  android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid ) {
                name=info.processName;
            }
        }
        return name;
    }

    public static boolean checkNetworkEnable(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cManager.getActiveNetworkInfo();
        if (network == null)
            return false;
        return network.isAvailable();
    }
    public static String getFileUriStr(String filePath) {
        Uri uri=Uri.fromFile(new File(filePath));
        return uri.toString();
    }

    /**
     * 隐藏软键盘
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        if(activity != null){
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()){
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }
}
