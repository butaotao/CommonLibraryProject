package com.dachen.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.dachen.common.R;

public class RatioRelativeLayout extends RelativeLayout{

	private float mRatio;

	public RatioRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.RatioRelativeLayout);
		mRatio=a.getFloat(R.styleable.RatioRelativeLayout_ratio, 0);
		a.recycle();
	}

	public RatioRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public RatioRelativeLayout(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(mRatio>0){
			int width=MeasureSpec.getSize(widthMeasureSpec);
			int height=(int) (width*mRatio);
			heightMeasureSpec=MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}
