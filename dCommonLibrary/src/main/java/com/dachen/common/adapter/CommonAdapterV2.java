package com.dachen.common.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mcp on 2016/8/9.
 */
public abstract class CommonAdapterV2<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mData;

    public CommonAdapterV2(Context mContext) {
        this(mContext, null);
    }
    public CommonAdapterV2(Context mContext, List<T> mData) {
        this.mContext = mContext;
        if(mData==null)
            mData=new ArrayList<>();
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public List<T> getData() {
        return mData;
    }
    public void update(List<T> data){
        mData.clear();
        if(data!=null)
            mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

}
