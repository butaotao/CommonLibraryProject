package com.dachen.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.dachen.common.utils.Logger;

import java.util.Stack;

/**
 * (应用程序Activity管理类：用于Activity管理和应用程序退出)
* @author yehj  V1.1.0
* @since : 2015-7-30 下午5:21:26
*
 */
public class AppManager {
	
	private static Stack<Activity> activityStack;
	private static AppManager instance;
	
	private AppManager(){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
	}
	/**
	 * 单一实例
	 */
	public synchronized static AppManager getAppManager(){
		if(instance==null){
			instance=new AppManager();
		}
		return instance;
	}
	/**
	 * 添加Activity到堆栈  
	 */
	public void addActivity(Activity activity){
		activityStack.add(activity);
		Logger.e("AppManager", activity.getLocalClassName());
	}
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	public void removeActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
		}
	}
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls){
		int size = activityStack.size();
		for(int i= size -1; i>=0; i--){
			Activity activity = activityStack.get(i);
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {	}
	}
	
	/**
	 * 显示指定的Activity（把指定Activity至于栈顶）
	 */
	public void showActivity(Class<?> cls){
		int size = activityStack.size();
		for(int i= size -1; i>=0; i--){
			Activity activity = activityStack.get(i);
			if(activity.getClass().equals(cls) ){
				break;
			}
			finishActivity(activity);
		}
	}	
	
}