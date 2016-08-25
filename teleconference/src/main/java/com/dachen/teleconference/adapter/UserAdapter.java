package com.dachen.teleconference.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.teleconference.R;
import com.dachen.teleconference.bean.User;
import com.dachen.teleconference.constants.ImageLoaderConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author gzhuo
 * @date 2016/8/19
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.GalleryViewHolder> {
    private Context mContext;
    private List<User> mData;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public UserAdapter(Context context, List<User> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_item_user, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        if (position == 0) {
            holder.mName.setText("添加");
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.blue_3cbaff));
            holder.mHeadImage.setImageResource(R.drawable.add_member_iv);
        } else {
            User user = mData.get(position - 1);
            holder.mName.setText(user.name);
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.black_333333));
            holder.mHeadImage.setImageResource(R.drawable.ic_launcher);
            ImageLoader.getInstance().displayImage(user.head, holder.mHeadImage, ImageLoaderConfig.mCircleImageOptions);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        public ImageView mHeadImage;
        public TextView mName;

        public GalleryViewHolder(final View itemView) {
            super(itemView);
            mHeadImage = (ImageView) itemView.findViewById(R.id.headImage);
            mName = (TextView) itemView.findViewById(R.id.name);
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v, getPosition());
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
