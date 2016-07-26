package com.dachen.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.R;
import com.dachen.common.utils.DisplayUtil;

/**
 * 
 * @author gaozhuo
 * @date 2015年12月23日
 */
public class CallHeadView extends RelativeLayout {
	private Paint mPaint;
	private ImageView mTopImage;
	private ImageView mLeftImage;
	private ImageView mRightImage;
	private TextView mLeftLabel;
	private TextView mRightLabel;
	private TextView  disturb_label;
	private Context mContext;

	public CallHeadView(Context context) {
		this(context, null);
	}

	public CallHeadView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CallHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.call_head_view, this);
		mTopImage = (ImageView) findViewById(R.id.top_image);
		mLeftImage = (ImageView) findViewById(R.id.left_image);
		mRightImage = (ImageView) findViewById(R.id.right_image);
		mLeftLabel = (TextView) findViewById(R.id.left_label);
		mRightLabel = (TextView) findViewById(R.id.right_label);
		disturb_label=(TextView) findViewById(R.id.disturb_label);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(0xff789ccd);
		mPaint.setStrokeWidth(6);

		setWillNotDraw(false);

	}

	public void setLeftLabel(String label) {
		mLeftLabel.setText(label);
	}

	public void setRightLabel(String label) {
		mRightLabel.setText(label);
	}

	public ImageView getLeftImage() {
		return mLeftImage;
	}

	public ImageView getRightImage() {
		return mRightImage;
	}

	public TextView getDisturbLabel(){
		return disturb_label;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int[] location1 = getCenter(mTopImage);
		int[] location2 = getCenter(mLeftImage);
		int[] location3 = getCenter(mRightImage);

		int x1 = location1[0] - DisplayUtil.dip2px(mContext, 28);
		int y1 = location1[1] + mTopImage.getHeight() / 2 - DisplayUtil.dip2px(mContext, 8);

		int x2 = location1[0] + DisplayUtil.dip2px(mContext, 28);
		int y2 = y1;

		int x3 = location2[0] + DisplayUtil.dip2px(mContext, 28);
		int y3 = location2[1] - mLeftImage.getHeight() / 2 + DisplayUtil.dip2px(mContext, 8);

		int x4 = location3[0] - DisplayUtil.dip2px(mContext, 28);
		int y4 = location3[1] - mRightImage.getHeight() / 2 + DisplayUtil.dip2px(mContext, 8);

		canvas.drawLine(x1, y1, x3, y3, mPaint);
		canvas.drawLine(x2, y2, x4, y4, mPaint);

	}

	private void calculateRelativeLocation(int[] location) {
		int[] thisViewLoction = new int[2];
		getLocationOnScreen(thisViewLoction);

		location[0] -= thisViewLoction[0];
		location[1] -= thisViewLoction[1];
	}

	private int[] getLocationInRootParent(View v) {
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		calculateRelativeLocation(location);
		return location;
	}

	private int[] getCenter(View v) {
		int[] location = getLocationInRootParent(v);
		location[0] += v.getWidth() / 2;
		location[1] += v.getHeight() / 2;

		return location;
	}

}
