package com.dachen.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 通用适配器，适用于item为多布局情况，比如聊天界面
 * @author gaozhuo
 * @since : 2015年9月28日
 *
 * @param <T>
 */
public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T> {

	protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

	public MultiItemCommonAdapter(Context context, List<T> data, MultiItemTypeSupport<T> multiItemTypeSupport) {
		super(context, data, -1);
		mMultiItemTypeSupport = multiItemTypeSupport;
	}

	@Override
	public int getViewTypeCount() {
		if (mMultiItemTypeSupport != null) {
			return mMultiItemTypeSupport.getViewTypeCount();
		}
		return super.getViewTypeCount();
	}

	@Override
	public int getItemViewType(int position) {
		if (mMultiItemTypeSupport != null) {
			return mMultiItemTypeSupport.getItemViewType(position, mData.get(position));
		}
		return super.getItemViewType(position);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mMultiItemTypeSupport == null) {
			return super.getView(position, convertView, parent);
		}

		int layoutId = mMultiItemTypeSupport.getLayoutId(position, getItem(position));
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, layoutId, position);
		bind(viewHolder, getItem(position));
		return viewHolder.getConvertView();
	}

}
