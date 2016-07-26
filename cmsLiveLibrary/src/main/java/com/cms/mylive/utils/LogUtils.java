package com.cms.mylive.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/*
 * 日志打印类
 */
public class LogUtils {

	private boolean isLog;
	private static LogUtils mLogUtils;

	public static LogUtils getInstance() {
		if (mLogUtils == null)
			mLogUtils = new LogUtils();
		return mLogUtils;
	}

	public void setLog(boolean isLog) {
		this.isLog = isLog;
	}

	public void logI(String msg) {
		if (isLog)
			Log.i("TEST", msg);
	}

	public void logI(Context context, String msg) {
		if (isLog)
			Log.i(context.getPackageName(), msg);
	}

	public void logD(String msg) {
		if (isLog)
			Log.d("TEST", msg);
	}

	public void logD(Context context, String msg) {
		if (isLog)
			Log.d(context.getPackageName(), msg);
	}

	public void toast(Context context, String msg) {
		if (isLog)
			Toast.makeText(context, msg, 0).show();
	}

}
