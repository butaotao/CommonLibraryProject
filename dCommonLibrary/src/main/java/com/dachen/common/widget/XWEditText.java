package com.dachen.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class XWEditText extends EditText{
	private XWEditText mthis;
	Context mContext;

	public XWEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mthis = this;
		mContext = context;
	}
	public XWEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mthis = this;
		mContext = context;
	}
	

	public XWEditText(Context context) {
		super(context);
		mthis = this;
		mContext = context;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if(e.getAction()==MotionEvent.ACTION_DOWN){
			
			getParent().requestDisallowInterceptTouchEvent(true);
		}else if(e.getAction()==MotionEvent.ACTION_MOVE){
			
			getParent().requestDisallowInterceptTouchEvent(true);
		}else if(e.getAction()==MotionEvent.ACTION_UP){
		}
		return super.onTouchEvent(e);
	}

}
