package com.cms.mylive.callbacks;

import android.util.Log;

import com.gensee.callback.IVideoCallBack;
import com.gensee.routine.UserInfo;

public class MyIVideoCallBack implements IVideoCallBack {
	private static final String TAG = "CallBack";

	@Override
	public void onVideoActived(UserInfo arg0, boolean arg1) {
		Log.d(TAG, "onViedoActived user=" + arg0.getName() + " userId=" + arg0.getId() + " bActived=" + arg1);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoCameraAvailiable(boolean arg0) {
		Log.d(TAG, "onVideoCameraAvailiable");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoCameraClosed() {
		Log.d(TAG, "onVideoCameraClosed");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoCameraOpened() {
		Log.d(TAG, "onVideoCameraOpened");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoDataRender(long arg0, int arg1, int arg2, int arg3,
			float arg4, byte[] arg5) {
		//Log.d(TAG, "onVideoDataRender");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoDisplay(UserInfo arg0) {
		Log.d(TAG, "onVideoDisplay");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoJoin(UserInfo arg0) {
		Log.d(TAG, "onVideoJoin");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoJoinConfirm(boolean arg0) {
		Log.d(TAG, "onVideoJoinConfirm");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoLeave(long arg0) {
		Log.d(TAG, "onVideoLeave");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoUndisplay(long arg0) {
		Log.d(TAG, "onVideoUndisplay");
		// TODO Auto-generated method stub
		
	}


}
