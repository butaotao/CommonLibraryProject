package com.dachen.common.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lmc
 *
 */
public class ObserverUtil {

	private static final String TAG = ObserverUtil.class.getSimpleName();

	// object
	private static final CopyOnWriteArrayList<Object> listObject = new CopyOnWriteArrayList<Object>();

	// class
	private static final Map< Class<?>, CopyOnWriteArrayList<Observer> > mapClass = new HashMap< Class<?>, CopyOnWriteArrayList<Observer> >();

	/**
	 * 添加被观察者对象
	 * 
	 * @param object
	 */
	public void addObserverObject(Object object) {
		if (object == null) {
			return;
		}

		//		Log.w(TAG, "addObserverObject():object:"+object.getClass().getName());
		listObject.add(object);
	}

	/**
	 * 移除被观察者对象
	 * 
	 * @param object
	 */
	public void removeObserverObject(Object object) {
		if (object == null) {
			return;
		}

		//		Log.w(TAG, "removeObserverObject():object:"+object.getClass().getName());
		listObject.remove(object);
	}

	/**
	 * 清空全部observerObject
	 */
	private void clearObserverObject() {
		listObject.clear();
	}

	/**
	 * 添加Observer
	 * 
	 * @param observerClass 
	 * @param observer 事件
	 */
	public void addObserver(Class<?> observerClass, Observer observer) {

		if (observerClass == null || observer == null) {
			return;
		}

		CopyOnWriteArrayList<Observer> list = null;
		list = mapClass.get(observerClass);
		if (list != null) {
			//			Log.w(TAG, "addObserver():observerClass:"+observerClass.getName()+",observer:"+observer.getClass().getName());
			list.add(observer);
		}else{
			// new List
			list = new CopyOnWriteArrayList<Observer>();
			//			Log.w(TAG, "addObserver():observerClass:"+observerClass.getName()+",observer:"+observer.getClass().getName());
			list.add(observer);
			mapClass.put(observerClass, list);
		}
	}

	/**
	 * 发送Observer
	 * 
	 * @param observerClass
	 */
	public void sendObserver(Class<?> observerClass, int what, int arg1, int arg2, Object dataObject) {
		if (observerClass == null) {
			return;
		}

		for (Object object : listObject) {
			if (object.getClass().equals(observerClass)) { // 比较class
				CopyOnWriteArrayList<Observer> list = null;
				list = mapClass.get(observerClass);
				if (list != null) {
					for (Observer o : list) {
						if (o != null) {
							// 调用
							Log.w(TAG, "sendObserver():object:"+object.getClass().getName());
							Log.w(TAG, "sendObserver():observerClass:"+observerClass.getName()+",what:"+what+",arg1:"+arg1+",arg2:"+arg2);
							if (dataObject != null) {
								Log.w(TAG, "sendObserver():dataObject:"+dataObject.getClass().getName());
							}
							o.onEvent(object, what, arg1, arg2, dataObject);
						}
					}
				}
			}
		}
	}

	public void sendObserver(Object observerObject, int what, int arg1, int arg2, Object dataObject) {
		if (observerObject == null) {
			return;
		}

		for (Object object : listObject) {
			if (object.equals(observerObject)) { // 比较object
				CopyOnWriteArrayList<Observer> list = null;
				list = mapClass.get(observerObject.getClass());
				if (list != null) {
					for (Observer o : list) {
						if (o != null) {
							// 调用
							Log.w(TAG, "sendObserver():object:"+object.getClass().getName());
							Log.w(TAG, "sendObserver():observerObject:"+observerObject.getClass().getName()+",what:"+what+",arg1:"+arg1+",arg2:"+arg2);
							if (dataObject != null) {
								Log.w(TAG, "sendObserver():dataObject:"+dataObject.getClass().getName());
							}
							o.onEvent(object, what, arg1, arg2, dataObject);
						}
					}
				}
			}
		}
	}

	public void sendObserver(Object observerObject, int what, Object dataObject) {
		sendObserver(observerObject, what, 0, 0, dataObject);
	}

	public void sendObserver(Class<?> observerClass, int what, Object dataObject) {
		sendObserver(observerClass, what, 0, 0, dataObject);
	}

	/**
	 * 移除Observer
	 * 
	 * @param observerClass 
	 * @param observer
	 */
	public void removeObserver(Class<?> observerClass, Observer observer) {

		if (observerClass == null || observer == null) {
			return;
		}

		CopyOnWriteArrayList<Observer> list = null;
		list = mapClass.get(observerClass);
		if (list != null) {
			for (Observer o : list) {
				if (o.equals(observer)) {
					//					Log.w(TAG, "removeObserver():observerClass:"+observerClass.getName()+",observer:"+observer.getClass().getName());
					list.remove(observer);
				}
			}
		}
	}

	/**
	 * 清空全部mapClass
	 */
	private void clearObserver() {
		mapClass.clear();
	}

	/**
	 * 清空全部
	 */
	public void clear() {
		clearObserverObject();
		clearObserver();
	}

	public Object getObject(Class<?> clazz) {
		for (Object object : listObject) {
			if (object.getClass() == clazz) {
				return object;
			}
		}
		return null;
	}

}
