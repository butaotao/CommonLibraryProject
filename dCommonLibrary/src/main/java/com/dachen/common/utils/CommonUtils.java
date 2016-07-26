package com.dachen.common.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

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
}
