package com.dachen.common.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 获取系统设备信息的工具类
 *
 * @author dty
 */
public class DeviceInfoUtil {
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /**
     * 判断是否为miui系统
     *
     * @return
     */
    public static boolean isMIUI() {
        return BuildPropertiesUtils.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || BuildPropertiesUtils.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || BuildPropertiesUtils.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
    }

    public static int checkOp(Context context, int op) {
        if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class c = appOpsManager.getClass();
            try {
                Class[] classes = new Class[]{int.class, int.class, String.class};
                Method method = c.getDeclaredMethod("checkOp", classes);
                return (Integer) method.invoke(appOpsManager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("", "below API 19 cannot invoke!");
        }
        return -1;
    }

    /**
     * 判断是否开启了悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean isFloatWindowOpAllowed(Context context) {
        if (!isMIUI()) {//非miui系统默认有悬浮窗权限
            return true;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return checkOp(context, 24) == AppOpsManager.MODE_ALLOWED ? true : false;
        } else {
            return (context.getApplicationInfo().flags & 0x8000000) == 1 << 27;
        }
    }

    /**
     * 判断手机是否处于锁屏状态
     *
     * @param context
     * @return true-锁屏状态;false-非锁屏状态
     */
    public static boolean isScreenLocked(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    /* 获取手机唯一序列号 */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();// 手机设备ID，这个ID会被用为用户访问统计
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        return deviceId;
    }

    /* 获取操作系统版本号 */
    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /* 获取手机型号 */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /* 获取app的版本信息 */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;// 系统版本号
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;// 系统版本名
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断应用是否运行在前台
     *
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 判断设备是否处于与用用户交互状态
     * @param context
     * @return
     */
    public static boolean isInteractive(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return pm.isInteractive();
        } else {
            return pm.isScreenOn();
        }
    }


}
