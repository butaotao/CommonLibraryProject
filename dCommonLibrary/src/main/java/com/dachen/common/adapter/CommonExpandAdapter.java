package com.dachen.common.adapter;

import android.content.Context;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mcp on 2016/8/9.
 */
public abstract class CommonExpandAdapter<T> extends BaseExpandableListAdapter {
    protected Context mContext;
    protected List<T> mData;

    public CommonExpandAdapter(Context mContext) {
        this(mContext, null);
    }
    public CommonExpandAdapter(Context mContext, List<T> mData) {
        this.mContext = mContext;
        if(mData==null)
            mData=new ArrayList<>();
        this.mData = mData;
    }

    @Override
    public int getGroupCount() {
        return  mData.size();
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
    public T getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
