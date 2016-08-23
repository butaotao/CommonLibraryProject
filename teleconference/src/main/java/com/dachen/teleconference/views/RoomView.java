package com.dachen.teleconference.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dachen.common.utils.DisplayUtil;
import com.dachen.teleconference.adapter.RoomPagerAdapter;
import com.dachen.teleconference.bean.User;

import java.util.List;
import com.dachen.teleconference.R;

/**
 * @author gzhuo
 * @date 2016/8/18
 */
public class RoomView extends RelativeLayout {
    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mDotContainer;

    private List<List<User>> mData;

    public RoomView(Context context) {
        super(context);
        init(context);
    }

    public RoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.room_view_layoyut, this, true);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        mDotContainer = (LinearLayout) findViewById(R.id.dot_container);
    }

    public void setData(List<List<User>> data) {
        if (mData != null) {
            throw new RuntimeException("data不能重复设置");
        }
        mData = data;
        mViewPager.setAdapter(new RoomPagerAdapter(mContext, data));
        addDot();
    }

    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            updateDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void updateDot(int position) {
        int childCount = mDotContainer.getChildCount();
        if (childCount <= 1) {
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View dot = mDotContainer.getChildAt(i);
            if (i == position) {
                dot.setBackgroundResource(R.drawable.shape_blue_dot);
            } else {
                dot.setBackgroundResource(R.drawable.shape_white_dot);
            }
        }
    }

    private void addDot() {
        int pageCount = mData.size();
        if (pageCount <= 1) {
            return;
        }
        mDotContainer.removeAllViews();
        for (int i = 0; i < pageCount; i++) {
            View dot = new View(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 10));
            lp.leftMargin = DisplayUtil.dip2px(mContext, 5);
            lp.rightMargin = DisplayUtil.dip2px(mContext, 5);
            dot.setLayoutParams(lp);
            dot.setBackgroundResource(R.drawable.shape_white_dot);
            mDotContainer.addView(dot);
        }

    }
}
