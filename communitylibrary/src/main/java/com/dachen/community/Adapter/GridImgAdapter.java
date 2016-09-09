package com.dachen.community.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dachen.community.R;
import com.dachen.community.model.PictureModel;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * [图片 适配器]
 *
 * @author zhouyuandong
 * @date 2016-9-9
 **/
public class GridImgAdapter extends BaseAdapter<PictureModel> {

    private ViewHolder holder;

    public GridImgAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_picture_layout, null);
            holder.mImageView = getViewById(convertView, R.id.iv_pic);
            holder.img_del = getViewById(convertView,R.id.img_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String netUrl = dataSet.get(position).getNetImg();
        String localUrl = dataSet.get(position).getLocalImg();

        if(TextUtils.isEmpty(netUrl)){//显示本地图片
            if(localUrl.equals("add")){
                holder.mImageView.setImageResource(R.drawable.add_pic);
            }else{
                ImageLoader.getInstance().displayImage("file://" + localUrl, holder.mImageView);
            }
        }else{//显示网络图片
            ImageLoader.getInstance().displayImage(netUrl,holder.mImageView);
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView mImageView;
        public ImageView img_del;
    }

}