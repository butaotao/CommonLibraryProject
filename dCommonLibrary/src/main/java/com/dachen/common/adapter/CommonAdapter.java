package com.dachen.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 通用适配器，适用于item为单一布局情况
 * 
 * @author gaozhuo
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mData;
	protected LayoutInflater mInflater;
	private int layoutId;
	
	public CommonAdapter(Context context, int layoutId) {
		this(context, null, layoutId);
	}

	public CommonAdapter(Context context, List<T> data, int layoutId) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		if (data != null) {
			this.mData = data;
		} else {
			this.mData = new ArrayList<T>();
		}
		this.layoutId = layoutId;
	}

	public void addData(T data) {
		if (mData != null) {
			mData.add(data);
			notifyDataSetChanged();
		}
	}

	public void  updata(Collection<T> data){
		if(mData!=null){
			mData.clear();
			mData.addAll(data);
			notifyDataSetChanged();
		}
	}
	public void addData(Collection<T> data) {
		if (mData != null) {
			mData.addAll(data);
			notifyDataSetChanged();
		}
	}

	public void addData(int location, T data) {
		if (mData != null) {
			mData.add(location, data);
			notifyDataSetChanged();
		}
	}

	public void addData(int location, Collection<T> data) {
		if (mData != null) {
			mData.addAll(location, data);
			notifyDataSetChanged();
		}
	}
	
	public void setDataSet(List<T> dataSet) {
		mData = dataSet;
	}
	
	public void clearData(){
		if (mData != null) {
			mData.clear();
		}
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, layoutId, position);
		bind(holder, getItem(position));
		return holder.getConvertView();
	}

	/**
	 * 把数据绑定到ViewHolder中
	 * 
	 * @param holder
	 * @param data
	 */
	public abstract void bind(ViewHolder holder, T data);
	
	public List<T> getDatas(){
		return mData;
	}

}
