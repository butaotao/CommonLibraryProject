package com.dachen.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.dachen.common.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyImageView extends ImageView {

	public float mRadius = 4;
	private String mScaleType = "";
	
	protected ImageLoader mImageLoader = ImageLoader.getInstance();
	
	public MyImageView(Context context) {
		super(context);
	}
	
    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    
    public void setImg(String url){
    	setImg(url,false);
    }
    
    public void setImg(String url,boolean isListener){
//    	if (mScaleType != null && mScaleType.equals("exactly")) {
//    		builder.imageScaleType(ImageScaleType.EXACTLY);
//    	} else {
//    		builder.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
//    	}    	
//    	DisplayImageOptions options = new DisplayImageOptions.Builder().build();
    	DisplayImageOptions options=null;
    		 options =  new DisplayImageOptions.Builder()
    		.showImageOnLoading(R.drawable.ic_launcher)
    		.showImageForEmptyUri(R.drawable.ic_launcher)
    		.showImageOnFail(R.drawable.ic_launcher)
    		.cacheInMemory(true)
    		.cacheOnDisc(true)
    		.considerExifParams(true)
    		.displayer(new RoundedBitmapDisplayer((int)mRadius))
    		.build();
    		 
    	if(isListener){
    		ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    		ImageLoader.getInstance().displayImage(url, this, options, animateFirstListener);
    	}else{
    		ImageLoader.getInstance().displayImage(url, this, options);
    	}
    }
    
    public void setImg(String url,int defaultImgResId){
    	setImg( url, defaultImgResId, null);
    }
    
    public void setImg(String url,int defaultImgResId,ImageLoadingListener animateFirstListener){
    	try{
    		DisplayImageOptions options =  new DisplayImageOptions.Builder()
    		.showImageOnLoading(defaultImgResId)
    		.showImageForEmptyUri(defaultImgResId)
    		.showImageOnFail(defaultImgResId)
    		.cacheInMemory(true)
    		.cacheOnDisc(true)
    		.considerExifParams(true)
    		.displayer(new RoundedBitmapDisplayer((int)mRadius))
    		.build();
    		if(animateFirstListener != null)
    			ImageLoader.getInstance().displayImage(url, this, options, animateFirstListener);
    		else
    			ImageLoader.getInstance().displayImage(url, this, options);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void setImg(String url,DisplayImageOptions options){
    	ImageLoader.getInstance().displayImage(url, this, options);
    }
    

    public void setImageResource(int resId) {
    	try {
    		super.setImageResource(resId);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }	

	private void init(Context context, AttributeSet attrs) {		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.nc) ;   
		//mRadius = a.getFloat(R.styleable.nc_radius, mRadius);  
		mRadius = a.getDimension(R.styleable.nc_roundRadius, mRadius);  
		    //Log.v(TAG, "mRadius: " + mRadius);
		mScaleType = a.getString(R.styleable.nc_scaleType);
		a.recycle() ;	
	}    
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
		
		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub
	
			super.onLoadingFailed(imageUri, view, failReason);
		}
		
	}	
    
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

	}
	
}
