package com.dachen.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by gzhuo on 2016/5/11.
 * 宽高比固定为16:9的ImageView
 */
public class FixedRatioImageView extends ImageView {
    public FixedRatioImageView(Context context) {
        super(context);
    }

    public FixedRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = widthSize * 9 / 16;
        setMeasuredDimension(widthSize, heightSize);
    }

}
