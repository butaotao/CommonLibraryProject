package com.dachen.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 自定义标签控件
 * 
 * @author lmc
 *
 */
public class TextTag extends Button {

	private boolean isSelected = false;

	private String tagName = null;

	public TextTag(Context context) {
		super(context);
		init(context);
	}

	public TextTag(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {

	}

	public boolean isSelected() {
		return isSelected;
	}

	public String getName() {
		return this.tagName;
	}

	public void setName(String tagName) {
		if (tagName != null) {
			this.tagName = tagName.trim();
		}
		this.setText(this.tagName);
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TextTag) {
			TextTag t = (TextTag)o;
			if (this.hashCode() == t.hashCode()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}

}
