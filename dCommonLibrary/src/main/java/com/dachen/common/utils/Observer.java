package com.dachen.common.utils;

/**
 * 观察者
 * 
 * @author lmc
 *
 */
public interface Observer {
	void onEvent(Object observer, int what, int arg1, int arg2, Object dataObject);
}
