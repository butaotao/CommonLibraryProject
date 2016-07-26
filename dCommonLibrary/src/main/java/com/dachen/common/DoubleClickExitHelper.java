package com.dachen.common;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;



/**
 * 
* @ClassName: DoubleClickExitHelper 
* @Description: TODO(双击退出) 
* @author yehj
* @date 2015-8-3 下午10:00:15 
* @version V1.0.0
 */
public class DoubleClickExitHelper {

	private final Activity mActivity;
	
	private boolean isOnKeyBacking;
	private Handler mHandler;
	private Toast mBackToast;
	
	public DoubleClickExitHelper(Activity activity) {
		mActivity = activity;
		mHandler = new Handler(Looper.getMainLooper());
	}
	
	/**
	 * Activity onKeyDown事件
	 * */
	public boolean onKeyDown(int keyCode, KeyEvent event,int appChannel) {
		if(keyCode != KeyEvent.KEYCODE_BACK) {
			return false;
		}
		if(isOnKeyBacking) {
			mHandler.removeCallbacks(onBackTimeRunnable);
			if(mBackToast != null){
				mBackToast.cancel();
			}
			// 退出
			AppManager.getAppManager().AppExit(mActivity);
			return true;
		} else {
			isOnKeyBacking = true;
			if(mBackToast == null) {
				if(appChannel==2){
					mBackToast = Toast.makeText(mActivity, R.string.back_exit_bd_tips, Toast.LENGTH_LONG);
				}else{
					mBackToast = Toast.makeText(mActivity, R.string.back_exit_tips, Toast.LENGTH_LONG);
				}
			}
			mBackToast.show();
			mHandler.postDelayed(onBackTimeRunnable, 2000);
			return true;
		}
	}
	
	private Runnable onBackTimeRunnable = new Runnable() {
		
		@Override
		public void run() {
			isOnKeyBacking = false;
			if(mBackToast != null){
				mBackToast.cancel();
			}
		}
	};
}
