package com.dachen.community.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dachen.community.R;
import com.dachen.community.activity.PublishActivity;
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
    private PictureModel mBean;
    private PublishActivity mActivity;

    public GridImgAdapter(Context mContext,PublishActivity activity) {
        super(mContext);
        mActivity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_picture_layout, null);
            holder.mImageView = getViewById(convertView, R.id.iv_pic);
            holder.img_del = getViewById(convertView,R.id.img_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(parent.getChildCount()==position){
         //正常的用于界面展示的View对象
            mBean = dataSet.get(position);
            String netUrl = dataSet.get(position).getNetImg();
            String localUrl = dataSet.get(position).getLocalImg();

            if(dataSet.get(position).isShowDel()){
                holder.img_del.setVisibility(View.VISIBLE);
            }else{
                holder.img_del.setVisibility(View.GONE);
            }

            if(TextUtils.isEmpty(netUrl)){//显示本地图片
                if(localUrl.equals("add")){
                    holder.mImageView.setImageResource(R.drawable.list_add);
                }else{
                    ImageLoader.getInstance().displayImage("file://" + localUrl, holder.mImageView);
                }
            }else{//显示网络图片
                ImageLoader.getInstance().displayImage(netUrl,holder.mImageView);
            }

            holder.img_del.setTag(position);
            holder.img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int)view.getTag();
                    mActivity.delPic(index);
                }
            });
        } else {
         //临时的position=0
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView mImageView;
        public ImageView img_del;
    }

}