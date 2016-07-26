package com.dachen.common.adapter;

/**
 * item多布局支持接口
 * 
 * @author gaozhuo
 * @date 2015年9月28日
 *
 * @param <T>
 */
public interface MultiItemTypeSupport<T> {
	int getLayoutId(int position, T data);

	int getViewTypeCount();

	int getItemViewType(int position, T data);
}