package com.example.teleconference.views;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.teleconference.R;


/**
 * 悬浮窗
 * 关于悬浮窗的权限问题详解下面的文章及里面的链接
 *
 * @author gzhuo
 * @date 2016/8/19
 * @see http://www.jianshu.com/p/167fd5f47d5c
 * @see com.dachen.common.utils.DeviceInfoUtil isFloatWindowOpAllowed
 */
public class FloatingView extends LinearLayout {
    private static final int PADDING = 20;
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager mWindowManager;
    private GestureDetectorCompat mGestureDetector;
    private float mRawX;
    private float mRawY;
    private float mTouchDownX;
    private float mTouchDownY;
    private boolean mIsShowing;
    private int mScreenWidth;
    private int mScreenHeight;
    private final int mStatusBarHeight = getStatusBarHeight();
    private Point[] mPoints;//屏幕上6个点的位置(4顶点+2腰)


    public FloatingView(Context context) {
        this(context, null);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.floating_window, this);
        mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        mScreenWidth = point.x;
        mScreenHeight = point.y;
        mLayoutParams = buildParams();
        mGestureDetector = new GestureDetectorCompat(context, new MyGestureListener());
    }

    private void initPoints() {
        if (mPoints == null) {
            mPoints = new Point[6];
            for (int i = 0; i < mPoints.length; i++) {
                mPoints[i] = new Point();
            }
            mPoints[0].x = PADDING;
            mPoints[0].y = PADDING;
            mPoints[1].x = mScreenWidth - PADDING;
            mPoints[1].y = mPoints[0].y;
            mPoints[2].x = PADDING;
            mPoints[2].y = (mScreenHeight - mStatusBarHeight) >> 1;
            mPoints[3].x = mPoints[1].x;
            mPoints[3].y = mPoints[2].y;
            mPoints[4].x = PADDING;
            mPoints[4].y = mScreenHeight - mStatusBarHeight - PADDING;
            mPoints[5].x = mPoints[1].x;
            mPoints[5].y = mPoints[4].y;
        }
    }


    public void show() {
        if (!mIsShowing) {
            mWindowManager.addView(this, mLayoutParams);
            mIsShowing = true;
        }
    }

    public void dismiss() {
        if (mIsShowing) {
            mIsShowing = false;
            mWindowManager.removeView(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        final float rawX = event.getRawX();
        final float rawY = event.getRawY();
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = x;
                mTouchDownY = y;
                mRawX = rawX;
                mRawY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                mRawX = rawX;
                mRawY = rawY;
                updatePosition();
                break;
            case MotionEvent.ACTION_UP:
                moveToEdge();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void moveToEdge() {
        initPoints();
        int[] location = new int[2];
        getLocationOnScreen(location);

        //计算view的中心点在屏幕上的坐标，不含状态栏
        int centerX = location[0] + (getWidth() >> 1);
        int centerY = location[1] - mStatusBarHeight + (getHeight() >> 1);

        Point currentPoint = new Point(location[0], location[1] - mStatusBarHeight);
        int index = calculateNearestPointIndex(new Point(centerX, centerY));
        Point endPoint = getEndPoint(index);

        ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(), currentPoint, endPoint);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                mLayoutParams.x = point.x;
                mLayoutParams.y = point.y;
                if (mIsShowing) {
                    mWindowManager.updateViewLayout(FloatingView.this, mLayoutParams);
                }
            }
        });
        animator.start();

    }

    public static class PointEvaluator implements TypeEvaluator<Point> {

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            Point point = new Point();
            point.x = startValue.x + (int) (fraction * (endValue.x - startValue.x));
            point.y = startValue.y + (int) (fraction * (endValue.y - startValue.y));
            return point;
        }
    }

    /**
     * 计算最近的点在mPoints中的索引
     *
     * @param p
     * @return
     */
    private int calculateNearestPointIndex(Point p) {
        if (mPoints == null) {
            return 0;
        }
        int distance = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < mPoints.length; i++) {
            Point point = mPoints[i];
            int d = (int) distance(point, p);
            if (d < distance) {
                distance = d;
                index = i;
            }
        }
        return index;
    }

    private Point getEndPoint(int index) {
        Point point = mPoints[index];
        Point endPoint = new Point();
        switch (index) {
            case 0:
                endPoint.x = point.x;
                endPoint.y = point.y;
                break;
            case 1:
                endPoint.x = point.x - getWidth();
                endPoint.y = point.y;
                break;
            case 2:
                endPoint.x = point.x;
                endPoint.y = point.y - (getHeight() >> 1);
                break;
            case 3:
                endPoint.x = point.x - getWidth();
                endPoint.y = point.y - (getHeight() >> 1);
                break;
            case 4:
                endPoint.x = point.x;
                endPoint.y = point.y - getHeight();
                break;
            case 5:
                endPoint.x = point.x - getWidth();
                endPoint.y = point.y - getHeight();
                break;
        }
        return endPoint;
    }

    public static double distance(Point a, Point b) {
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void updatePosition() {
        mLayoutParams.x = (int) (mRawX - mTouchDownX);
        mLayoutParams.y = (int) (mRawY - getStatusBarHeight() - mTouchDownY);
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            dismiss();
            return super.onSingleTapConfirmed(e);
        }
    }

    private WindowManager.LayoutParams buildParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (Build.VERSION.SDK_INT >= 19) {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;
        params.x = 100;
        params.y = 100;
        params.gravity = Gravity.LEFT | Gravity.TOP;

        return params;
    }
}
