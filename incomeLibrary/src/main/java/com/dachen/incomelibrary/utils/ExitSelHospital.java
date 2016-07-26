package com.dachen.incomelibrary.utils;

import android.app.Activity;

import java.util.LinkedList;


public class ExitSelHospital {
	private LinkedList<Activity> activityList = new LinkedList();
	private static ExitSelHospital instance;

	private ExitSelHospital() {
	}

	// 单例模式中获取唯一的ExitSelHospital实例
	public static ExitSelHospital getInstance() {
		if (null == instance) {
			instance = new ExitSelHospital();
		}
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	//	System.exit(0);
	}
}
