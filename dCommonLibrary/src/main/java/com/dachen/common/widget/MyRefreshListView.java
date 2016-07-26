package com.dachen.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.dachen.common.R;
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.UIHelper;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

/**
 * 
* @ClassName: MyRefreshListView 
* @Description: TODO(扩展下拉刷新) 
* @author yehj
* @date 2015-8-6 上午12:28:17 
* @version V1.0.0
 */
public class MyRefreshListView extends PullToRefreshListView {
	
	private View netErrorView;//网络断开提示视图
	private View emptyView;//没有数据提示视图
	private Drawable emptyDrawable;//没有数据提示图片(xml中drawableEmpty的属性值)
	
	public MyRefreshListView(Context context) {
		super(context);
		Logger.d("yehj", "MyRefreshListView(Context context) ");
	}

	public MyRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	
	public MyRefreshListView(Context context, Mode mode) {
		super(context, mode);
	}

	public MyRefreshListView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}
	
	private void init(Context context, AttributeSet attrs) {		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.nc) ;
		emptyDrawable = a.getDrawable(R.styleable.nc_drawableEmpty);
		a.recycle() ;	
		initEmptyView(context, emptyDrawable);
	} 	
	
	public boolean isHeaderShown() {
	    return getHeaderLayout().isShown();
	}
	public boolean isFooterShown() {
	    return getFooterLayout().isShown();
	}		
	
	
	/**
	 * 
	* @date 2015-7-9
	* @version V1.3.0 
	* @author yehj
	* @Description: TODO(设置刷新动画的声音) 
	* @param @param context
	* @param @param resId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void  setSoundPullEventListener(Context context,State state ,int resId){
		SoundPullEventListener  soundPullEventListener=new SoundPullEventListener(context);
		soundPullEventListener.addSoundEvent(state, resId);
		setOnPullEventListener(soundPullEventListener);
	}
	
	/**
	 * 立刻滚动到指定位置
	 * @param selection
	 */
	public void setPosition(int selection){
		ListView mRefreshableView = getRefreshableView();
		if(mRefreshableView != null){
			mRefreshableView.setSelection(selection);
		}
	}
	
	public void pullDownRefresh(){
		setPosition(0);
		super.onRefreshing(false);
	}
	
	/**
	 * 手动清除加载提示
	 * @desc  一般触屏上下拉请使用onRefreshComplete方法
	 */
	public void reset(){
		super.onRefreshComplete();
//		super.onReset();(有bug 暂不使用)
	}
	
	@Override
	protected void onReleaseToRefresh() {
		// TODO Auto-generated method stub
		super.onReleaseToRefresh();
	}
	
	/**
	 * 上拉，没有更多数据加载
	 */
	public void noMoreLoaded(){
		this.post(new Runnable() {//tell me why?
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MyRefreshListView.this.onRefreshComplete();
			}
		});
		UIHelper.ToastMessage(getContext(), R.string.pull_to_refresh_nomore_label);
	}
	
	/**
	 * 下拉，没有数据
	 */
	public void refreshLvComplete(){
		this.post(new Runnable() {
			@Override
			public void run() {
				MyRefreshListView.this.onRefreshComplete();
			}
		});
	}
	
	public void disConnected(){
		this.post(new Runnable() {//tell me why?
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MyRefreshListView.this.onRefreshComplete();
			}
		});
//		showNetErrorView();
		UIHelper.ToastMessage(getContext(), R.string.network_not_connected);
	}
	
	/**
	 * 初始化没有数据提示
	 * @param context
	 * @param resId
	 */
	public void initEmptyView(Context context,int resId){
		emptyView = LayoutInflater.from(context).inflate(R.layout.empty, null);
		ImageView img = (ImageView) emptyView.findViewById(R.id.empty);
		img.setImageResource(resId);		
	}
	
	/**
	 * 初始化没有数据提示
	 * @param context
	 */
	public void initEmptyView(Context context,Drawable drawable){
		if(drawable == null)
			return;
		emptyView = LayoutInflater.from(context).inflate(R.layout.empty, null);
		ImageView img = (ImageView) emptyView.findViewById(R.id.empty);
		img.setImageDrawable(drawable);		
	}
	
	/**
	 * 显示没有数据视图
	 */
	public void showEmtyView(){
		if(emptyView != null){
			setEmptyView(emptyView);	
		}
		if(netErrorView != null)
			netErrorView.setVisibility(View.INVISIBLE);		
	}
	
	/**
	 * 显示网络断开视图
	 */
	public void showNetErrorView(){
		if(netErrorView != null)
			setEmptyView(netErrorView);		
		if(emptyView != null)
			emptyView.setVisibility(View.INVISIBLE);
	}
	
}
