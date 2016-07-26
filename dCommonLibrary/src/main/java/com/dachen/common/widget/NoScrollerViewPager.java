/*
    ShengDao Android Client, NoScrollerViewPager
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.dachen.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * [不滑动的ViewPager]
 * 
 * @author devin.hu
 * @version 1.0
 * @date 2014-11-17
 * 
 **/
public class NoScrollerViewPager extends ViewPager {

	private boolean enabled;

	/**
	 * @param context
	 */
	public NoScrollerViewPager(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public NoScrollerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

}
