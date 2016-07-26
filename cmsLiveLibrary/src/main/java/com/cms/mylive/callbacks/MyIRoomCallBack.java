package com.cms.mylive.callbacks;

import android.content.Context;
import android.util.Log;

import com.gensee.callback.IRoomCallBack;
import com.gensee.common.ServiceType;
import com.gensee.routine.State;
import com.gensee.routine.UserInfo;

public class MyIRoomCallBack implements IRoomCallBack {
	private static final String TAG = "CallBack";

	@Override
	public void OnUpgradeNotify(String arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "OnUpgradeNotify");
	}

	@Override
	public ServiceType getServiceType() {
		// TODO Auto-generated method stub
		Log.d(TAG, "getServiceType");
		return null;
	}

	@Override
	public void onChatMode(int arg0) {
		Log.d(TAG, "onChatMode");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFreeMode(boolean arg0) {
		Log.d(TAG, "onFreeMode");
		// TODO Auto-generated method stub
		
	}

	@Override
	public Context onGetContext() {
		Log.d(TAG, "onGetContext");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInit(boolean arg0) {
		Log.d(TAG, "onInit");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJoin(boolean arg0) {
		Log.d(TAG, "onJoin");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLottery(byte arg0, String arg1) {
		Log.d(TAG, "onLottery");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomBroadcastMsg(String arg0) {
		Log.d(TAG, "onRoomBroadcastMsg");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomData(String arg0, long arg1) {
		Log.d(TAG, "onRoomData");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomHanddown(long arg0) {
		Log.d(TAG, "onRoomHanddown");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomHandup(long arg0, String arg1) {
		Log.d(TAG, "onRoomHandup");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomJoin(int arg0, UserInfo arg1) {
		Log.d(TAG, "onRoomJoin");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomLeave(int arg0) {
		Log.d(TAG, "onRoomLeave");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomLock(boolean arg0) {
		Log.d(TAG, "onRoomLock");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomPublish(State arg0) {
		Log.d(TAG, "onRoomPublish");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomReconnecting() {
		Log.d(TAG, "onRoomReconnecting");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomRecord(State arg0) {
		Log.d(TAG, "onRoomRecord");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomRollcall(int arg0) {
		Log.d(TAG, "onRoomRollcall");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomRollcallAck(long arg0) {
		Log.d(TAG, "onRoomRollcallAck");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomSubject(String arg0) {
		Log.d(TAG, "onRoomSubject");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomUserJoin(UserInfo arg0) {
		Log.d(TAG, "onRoomUserJoin");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomUserLeave(UserInfo arg0) {
		Log.d(TAG, "onRoomUserLeave");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomUserUpdate(UserInfo arg0) {
		Log.d(TAG, "onRoomUserUpdate");
		// TODO Auto-generated method stub
		
	}

	@Override
	public String onSettingAppPlatform() {
		Log.d(TAG, "onSettingAppPlatform");
		// TODO Auto-generated method stub
		return null;
	}

	
}
