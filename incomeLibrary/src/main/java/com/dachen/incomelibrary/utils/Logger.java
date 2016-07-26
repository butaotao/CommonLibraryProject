package com.dachen.incomelibrary.utils;


import android.util.Log;

/**
 * 
 * @ClassName:  Logger   
 * @Description:TODO(日志规范)   
 * @author: yehj  
 * @date:   2015-8-14 下午3:20:45   
 * @version V1.0.0
 */

public class Logger {
	
	public final static int LOG_LEVEL = 6;//开发模式为6，上线模式为0
	public final static int RELEASE = 0 ;//上线模式
	public final static int ERROR = 1;
	public final static int WARN = 2;
	public final static int INFO = 3;
	public final static int DEBUG = 4;
	public final static int VERBOS = 5;
	
	
	public static boolean isDebug(){
		int logLevel = Logger.LOG_LEVEL;
		return logLevel != RELEASE;		
	}

	public static void e(String tag, String msg) {
		if (LOG_LEVEL > ERROR)
			Log.e(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (LOG_LEVEL > WARN)
			Log.w(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (LOG_LEVEL > INFO)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (LOG_LEVEL > DEBUG)
			Log.d(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (LOG_LEVEL > VERBOS)
			Log.v(tag, msg);
	}


}



