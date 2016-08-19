package com.example.teleconference.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.teleconference.bean.User;

import java.util.List;

/**
 *
 *
 * @author gzhuo
 * @date 2016/8/18
 */
public class RoomPagerAdapter extends PagerAdapter {

    private List<List<User>> mData;
    private final Context mContext;
    private UserAdapter mAdapter;


    public RoomPagerAdapter(Context context, List<List<User>> data) {
        mData = data;
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        List<User> userList = mData.get(position);
        RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.addView(recyclerView, 0);

        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new GridDividerItemDecoration(2, 3));
        mAdapter = new UserAdapter(mContext, userList);
        mAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }
        });
        recyclerView.setAdapter(mAdapter);

        return recyclerView;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}

