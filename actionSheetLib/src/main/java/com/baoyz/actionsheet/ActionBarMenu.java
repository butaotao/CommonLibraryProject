package com.baoyz.actionsheet;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 
 * @author WANG
 * 
 */
public class ActionBarMenu extends LinearLayout  {

	private ArrayList<ActionMenu> items;
	private OnClickListener mListener;

	public ActionBarMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		setContentView();
	}

	public ActionBarMenu(Context context) {
		super(context);
		setContentView();
	}
	
	

	public void setmListener(OnClickListener mListener) {
		this.mListener = mListener;
	}

	public void setItems(ArrayList<ActionMenu> items) {
		this.items = items;
		init();
	}


	private void setContentView() {
		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.parseColor("#21292B"));

	}

	public void init() {

		LinearLayout item = null;

		for (int i = 0; i < items.size(); i++) {

			if (i == 0) {
				item = createItem(true);
			} else {
				item = createItem(false);
			}

			item.setBackgroundColor(Color.parseColor("#1A2020"));

			ActionMenu menu = items.get(i);

			ImageView iv = createImageView(menu.getResourceId());
			TextView tv = createTextView(menu.getTitle());

			item.addView(iv);
			item.addView(tv);

			item.setTag(i);
			item.setOnClickListener(mListener);
			this.addView(item);
		}
	}

	private LinearLayout createItem(boolean isFirstItem) {
		LinearLayout linearLayout = new LinearLayout(getContext());
//		android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
//				android.widget.LinearLayout.LayoutParams.FILL_PARENT,
//				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);

		LayoutParams params = new LayoutParams(
				300,
				LayoutParams.WRAP_CONTENT);
		
		if (!isFirstItem) {
			params.setMargins(0, 1, 0, 0);
		}
		linearLayout.setLayoutParams(params);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);
		linearLayout.setPadding(20, 10, 10, 10);

		return linearLayout;
	}

	private ImageView createImageView(int resId) {
		ImageView imageView = new ImageView(getContext());
		android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		imageView.setLayoutParams(params);
		imageView.setScaleType(ScaleType.FIT_CENTER);
		imageView.setImageResource(resId);

		return imageView;
	}

	private TextView createTextView(String text) {
		TextView textView = new TextView(getContext());
		android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(params);

		textView.setPadding(4, 0, 0, 0);
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(16);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setText(text);

		return textView;

	}



	
}
