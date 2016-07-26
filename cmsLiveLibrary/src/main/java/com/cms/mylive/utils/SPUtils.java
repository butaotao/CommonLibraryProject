package com.cms.mylive.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtils {
	private static SharedPreferences mSharedPreferences;
	public static SharedPreferences getInstance(Context context){
		if(mSharedPreferences==null)
			mSharedPreferences=context.getSharedPreferences("cmsaudioandvideo", Context.MODE_PRIVATE);
		return mSharedPreferences;
	}
	
	public static void putString(String key,String value){
		Editor edit = mSharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}
}
