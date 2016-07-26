package com.dachen.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.common.R;


/**
 * 左右分段控件
 * 
 * @author tianwei
 * 
 */
public class SegementView extends LinearLayout implements OnClickListener {

	private TextView mTvLeft;
	private TextView mTvRight;
	private boolean isLeftSelected = true;
	private OnSelectedListener mListener;

	public SegementView(Context context) {
		this(context, null);
	}

	public SegementView(Context context, AttributeSet attrs) {
		super(context, attrs);

		View.inflate(context, R.layout.view_segement, this);
		mTvLeft = (TextView) findViewById(R.id.view_segement_tv_left);
		mTvRight = (TextView) findViewById(R.id.view_segement_tv_right);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.nc);
		mTvLeft.setText(ta.getText(R.styleable.nc_segement_tv1));
		mTvRight.setText(ta.getText(R.styleable.nc_segement_tv2));
		// 设置左边默认选中
		mTvLeft.setSelected(true);

		// 设置点击事件
		mTvLeft.setOnClickListener(this);
		mTvRight.setOnClickListener(this);
		ta.recycle();
	}

	@Override
	public void onClick(View v) {
		if (v == mTvRight) {
			if (isLeftSelected) {
				mTvRight.setSelected(true);
				mTvLeft.setSelected(false);

				if (mListener != null) {
					mListener.onSelected(false);
				}

				isLeftSelected = false;
			}

		} else if (v == mTvLeft) {
			if (!isLeftSelected) {
				mTvRight.setSelected(false);
				mTvLeft.setSelected(true);

				if (mListener != null) {
					mListener.onSelected(true);
				}

				isLeftSelected = true;
			}
		}

	}

	public void changeSelectTitle(){
		mTvRight.setVisibility(GONE);
		mTvLeft.setBackgroundResource(R.color.action_bar_bg_color);
		mTvLeft.setTextColor(getResources().getColor(R.color.white));
		mTvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
	}

	/**
	 * 设置选中的监听
	 * 
	 * @param listener
	 */
	public void setOnSelectedListener(OnSelectedListener listener) {
		this.mListener = listener;
	}

	public interface OnSelectedListener {
		/**
		 * 当分段控件选中时的回调，如果左侧选中时，leftSelected为true，否则为false
		 * 
		 * @param leftSelected
		 */
		void onSelected(boolean leftSelected);
	}

}
