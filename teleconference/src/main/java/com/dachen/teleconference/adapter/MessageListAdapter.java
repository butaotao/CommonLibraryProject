package com.dachen.teleconference.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dachen.teleconference.R;

import java.util.List;

/**
 * Created by TianWei on 2016/8/29.
 */
public class MessageListAdapter extends BaseAdapter {

    private List<String> mData;
    private Context mContext;

    public MessageListAdapter(Context context) {
        mContext = context;
    }

    public MessageListAdapter(Context context, List<String> data) {
        mContext = context;
        mData = data;
    }

    public void setData(List<String> data) {
        mData = data;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_list, null);
            holder = new ViewHolder();
            holder.tvMessage = (TextView) convertView.findViewById(R.id.message_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String s = mData.get(position);
        if (s.endsWith("解除全员静音") || s.endsWith("开启全员静音")||s.endsWith("请把握好会议节奏")) {
            holder.tvMessage.setTextColor(Color.RED);
        }else{
            holder.tvMessage.setTextColor(mContext.getResources().getColor(R.color.gray_aaaaaa));
        }
        holder.tvMessage.setText(mData.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tvMessage;
    }

}
