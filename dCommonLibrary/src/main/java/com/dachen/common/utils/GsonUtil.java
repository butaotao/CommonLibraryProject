package com.dachen.common.utils;

import com.google.gson.Gson;

/**
 * 单例
 * 
 * @author WANG
 * 
 */
public class GsonUtil {
	private volatile static Gson uniqueInstance;

	public static Gson getGson() {
		if (uniqueInstance == null) {
			synchronized (GsonUtil.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new Gson();

				}
			}
		}
		return uniqueInstance;
	}
}
