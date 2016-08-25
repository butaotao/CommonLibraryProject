package com.dachen.teleconference.activity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.common.utils.Logger;
import com.dachen.common.utils.UIHelper;
import com.dachen.teleconference.AgoraManager;
import com.dachen.teleconference.MediaHandler;
import com.dachen.teleconference.MediaMessage;
import com.dachen.teleconference.R;
import com.dachen.teleconference.bean.GetMediaDynamicKeyResponse;
import com.dachen.teleconference.bean.GetSigningKeyResponse;
import com.dachen.teleconference.common.BaseActivity;
import com.dachen.teleconference.http.HttpCommClient;
import com.example.teleconference.RtcEngineEventHandlerMgr;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import io.agora.AgoraAPI;
import io.agora.AgoraAPIOnlySignal;
import io.agora.IAgoraAPI;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class AgoraVoiceSdkDemo extends BaseActivity implements OnClickListener{

	private static final String vendorKey="86c6c121ff444021a5152b0a791aefd3";
	public static final String signKey = "b9aec320141347c6b64226cb7e901d23";
	private ScrollView scrollView;
	private TextView channel_status;
	private EditText user_id;
	private EditText channel_id;
	private Button btn_ctrl,btn_switcher,btn_leave,btn_ctr2;
	private boolean isLogin,isLeave,isJoin;
	private final static int getMediaDynamicKey=1;
	private final static int getSignningKey=2;
	private Handler mHandler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case getMediaDynamicKey:
					if(msg.arg1==1){
						if(msg.obj!=null){
							AgoraManager.getInstance(AgoraVoiceSdkDemo.this).joinChannel(channel_id.getText().toString(),((GetMediaDynamicKeyResponse)msg.obj).getData(),Integer.parseInt(user_id.getText().toString()));
						}
					}else{
						UIHelper.ToastMessage(AgoraVoiceSdkDemo.this,(String)msg.obj);
					}
					break;
				case getSignningKey:
					if(msg.arg1==1){
						if(msg.obj!=null){
							long expiredTime = new Date().getTime()/1000 + 3600;
							String token = calcToken(vendorKey,signKey,user_id.getText().toString(), expiredTime);
							Logger.d("yehj","token=="+((GetSigningKeyResponse)msg.obj).getData());
							AgoraManager.getInstance(AgoraVoiceSdkDemo.this).loginAgora(user_id.getText().toString(),((GetSigningKeyResponse)msg.obj).getData(),vendorKey);
						}
					}else{
						UIHelper.ToastMessage(AgoraVoiceSdkDemo.this,(String)msg.obj);
					}
					break;

			}
		}
	};




	private IRtcEngineEventHandler  iRtcEngineEventHandler=new IRtcEngineEventHandler() {
		@Override
		public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
			Logger.d("yehj","onJoinChannelSuccess---channel"+channel);
		}

		@Override
		public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
			Logger.d("yehj","onRejoinChannelSuccess---channel"+channel);

		}

		@Override
		public void onWarning(int warn) {
			Logger.d("yehj","warn---"+warn);
		}

		@Override
		public void onError(int err) {
			Logger.d("yehj","onError---"+err);
		}

		@Override
		public void onApiCallExecuted(String api, int error) {
			Logger.d("yehj","onApiCallExecuted---"+error);
		}

		@Override
		public void onCameraReady() {
			Logger.d("yehj","onCameraReady");
		}

		@Override
		public void onVideoStopped() {
			Logger.d("yehj","onVideoStopped");

		}

		@Override
		public void onAudioQuality(int uid, int quality, short delay, short lost) {
			Logger.d("yehj","onAudioQuality");
		}

		@Override
		public void onLeaveChannel(RtcStats stats) {
			Logger.d("yehj","onLeaveChannel");
		}

		@Override
		public void onRtcStats(RtcStats stats) {
			Logger.d("yehj","onRtcStats");
		}

		@Override
		public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
			Logger.d("yehj","onAudioVolumeIndication");
		}

		@Override
		public void onNetworkQuality(int quality) {
			Logger.d("yehj","onNetworkQuality");
		}

		@Override
		public void onUserJoined(int uid, int elapsed) {
			Logger.d("yehj","onUserJoined");
		}

		@Override
		public void onUserOffline(int uid, int reason) {
			Logger.d("yehj","onUserJoined");

		}

		@Override
		public void onUserMuteAudio(int uid, boolean muted) {
			Logger.d("yehj","onUserMuteAudio");
		}

		@Override
		public void onUserMuteVideo(int uid, boolean muted) {
			Logger.d("yehj","onUserMuteVideo");
		}

		@Override
		public void onUserEnableVideo(int uid, boolean enabled) {
			Logger.d("yehj","onUserEnableVideo");
		}

		@Override
		public void onLocalVideoStat(int sentBitrate, int sentFrameRate) {
			Logger.d("yehj","onLocalVideoStat");
		}

		@Override
		public void onRemoteVideoStat(int uid, int delay, int receivedBitrate, int receivedFrameRate) {
			Logger.d("yehj","onRemoteVideoStat");
		}

		@Override
		public void onRemoteVideoStats(RemoteVideoStats stats) {
			Logger.d("yehj","onRemoteVideoStats");
		}

		@Override
		public void onLocalVideoStats(LocalVideoStats stats) {
			Logger.d("yehj","onLocalVideoStats");

		}

		@Override
		public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {
			Logger.d("yehj","onFirstRemoteVideoFrame");
		}

		@Override
		public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
			Logger.d("yehj","onFirstLocalVideoFrame");
		}

		@Override
		public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
			Logger.d("yehj","onFirstRemoteVideoDecoded");
		}

		@Override
		public void onConnectionLost() {
			Logger.d("yehj","onConnectionLost");
		}

		@Override
		public void onConnectionInterrupted() {
			Logger.d("yehj","onConnectionInterrupted");
		}

		@Override
		public void onRefreshRecordingServiceStatus(int status) {
			Logger.d("yehj","onRefreshRecordingServiceStatus");

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);
		initView();
		initAgoraConfigure();
	}
	private void initView(){
		scrollView=(ScrollView)findViewById(R.id.scrollview);
		channel_status=(TextView)findViewById(R.id.channel_status);
		user_id=(EditText)findViewById(R.id.user_id);
		channel_id=(EditText)findViewById(R.id.channel_id);
		btn_ctrl=(Button)findViewById(R.id.btn_ctrl);
		btn_ctr2=(Button)findViewById(R.id.btn_ctr2);
		btn_switcher=(Button)findViewById(R.id.btn_switcher);
		btn_leave=(Button)findViewById(R.id.btn_leave);
		btn_ctrl.setOnClickListener(this);
		btn_ctr2.setOnClickListener(this);
		btn_leave.setOnClickListener(this);
		btn_switcher.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_ctrl ){
			HttpCommClient.getInstance().getMediaDynamicKey(this,mHandler,getMediaDynamicKey,user_id.getText().toString(),channel_id.getText().toString(),"3600");

		}else if(v.getId() == R.id.btn_leave){

		}else if(v.getId() == R.id.btn_switcher){

		}else if(v.getId() == R.id.btn_ctr2){
			if(TextUtils.isEmpty(user_id.getText().toString())||TextUtils.isEmpty(channel_id.getText().toString())){
				UIHelper.ToastMessage(this,"userid或者channel_id不能为空");
				return;
			}
			if(!isLogin){
				HttpCommClient.getInstance().getSigningKey(this,mHandler,getSignningKey,user_id.getText().toString(),"3600");
			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AgoraManager.getInstance(this).getEventHandlerMgr().addRtcEngineEventHandler(iRtcEngineEventHandler);
	}

	@Override
	protected void onPause() {
		super.onPause();
		AgoraManager.getInstance(this).getEventHandlerMgr().removeRtcEngineEventHandler(iRtcEngineEventHandler);
	}

	private void initAgoraConfigure(){
		AgoraManager.getInstance(this).initAgora(vendorKey);
//		mAgoraAPIOnlySignal.callbackSet(new AgoraAPI.CallBack(){
//
//			@Override
//			public void setCB(IAgoraAPI.ICallBack cb) {
//				super.setCB(cb);
//			}
//
//			@Override
//			public IAgoraAPI.ICallBack getCB() {
//				return super.getCB();
//			}
//
//			@Override
//			public void onReconnecting(int nretry) {
//				super.onReconnecting(nretry);
//			}
//
//			@Override
//			public void onReconnected(int fd) {
//				super.onReconnected(fd);
//			}
//
//			@Override
//			public void onLoginSuccess(int uid, int fd) {
//				super.onLoginSuccess(uid, fd);
//				Logger.d("yehj","onLoginSuccess");
//				isLogin=true;
//			}
//
//			@Override
//			public void onLoginFailed(int ecode) {
//				super.onLoginFailed(ecode);
//				Logger.d("yehj","onLoginFailed");
//			}
//
//			@Override
//			public void onLogout(int ecode) {
//				super.onLogout(ecode);
//				Logger.d("yehj","onLogout");
//				isLogin=false;
//			}
//
//			@Override
//			public void onChannelJoined(String channelID) {
//				super.onChannelJoined(channelID);
//				Logger.d("yehj","onChannelJoined------channelID---"+channelID);
//			}
//
//			@Override
//			public void onChannelJoinFailed(String channelID, int ecode) {
//				super.onChannelJoinFailed(channelID, ecode);
//				Logger.d("yehj","onChannelJoinFailed------channelID---"+channelID);
//			}
//
//			@Override
//			public void onChannelLeaved(String channelID, int ecode) {
//				super.onChannelLeaved(channelID, ecode);
//				Logger.d("yehj","onChannelLeaved------channelID---"+channelID);
//
//			}
//
//			@Override
//			public void onChannelUserJoined(String account, int uid) {
//				super.onChannelUserJoined(account, uid);
//				Logger.d("yehj","onChannelUserJoined------account---"+account);
//			}
//
//			@Override
//			public void onChannelUserLeaved(String account, int uid) {
//				super.onChannelUserLeaved(account, uid);
//				Logger.d("yehj","onChannelUserLeaved------account---"+account);
//			}
//
//			@Override
//			public void onChannelUserList(String[] accounts, int[] uids) {
//				super.onChannelUserList(accounts, uids);
//				Logger.d("yehj","onChannelUserList");
//			}
//
//			@Override
//			public void onChannelQueryUserNumResult(String channelID, int ecode, int num) {
//				super.onChannelQueryUserNumResult(channelID, ecode, num);
//				Logger.d("yehj","onChannelQueryUserNumResult");
//			}
//
//			@Override
//			public void onChannelAttrUpdated(String channelID, String name, String value, String type) {
//				super.onChannelAttrUpdated(channelID, name, value, type);
//				Logger.d("yehj","onChannelAttrUpdated");
//			}
//
//			@Override
//			public void onInviteReceived(String channelID, String account, int uid) {
//				super.onInviteReceived(channelID, account, uid);
//				Logger.d("yehj","onInviteReceived===account-----"+account);
//			}
//
//			@Override
//			public void onInviteReceivedByPeer(String channelID, String account, int uid) {
//				super.onInviteReceivedByPeer(channelID, account, uid);
//				Logger.d("yehj","onInviteReceivedByPeer===account-----"+account);
//			}
//
//			@Override
//			public void onInviteAcceptedByPeer(String channelID, String account, int uid) {
//				super.onInviteAcceptedByPeer(channelID, account, uid);
//				Logger.d("yehj","onInviteAcceptedByPeer===account-----"+account);
//			}
//
//			@Override
//			public void onInviteRefusedByPeer(String channelID, String account, int uid) {
//				super.onInviteRefusedByPeer(channelID, account, uid);
//				Logger.d("yehj","onInviteRefusedByPeer===account-----"+account);
//			}
//
//			@Override
//			public void onInviteFailed(String channelID, String account, int uid, int ecode) {
//				super.onInviteFailed(channelID, account, uid, ecode);
//				Logger.d("yehj","onInviteFailed===account-----"+account);
//			}
//
//			@Override
//			public void onInviteEndByPeer(String channelID, String account, int uid) {
//				super.onInviteEndByPeer(channelID, account, uid);
//				Logger.d("yehj","onInviteEndByPeer===account-----"+account);
//			}
//
//			@Override
//			public void onInviteEndByMyself(String channelID, String account, int uid) {
//				super.onInviteEndByMyself(channelID, account, uid);
//				Logger.d("yehj","onInviteEndByMyself===account-----"+account);
//			}
//
//			@Override
//			public void onMessageSendError(String messageID, int ecode) {
//				super.onMessageSendError(messageID, ecode);
//				Logger.d("yehj","onMessageSendError===messageID-----"+messageID);
//			}
//
//			@Override
//			public void onMessageSendSuccess(String messageID) {
//				super.onMessageSendSuccess(messageID);
//				Logger.d("yehj","onMessageSendSuccess===messageID-----"+messageID);
//			}
//
//			@Override
//			public void onMessageAppReceived(String msg) {
//				super.onMessageAppReceived(msg);
//				Logger.d("yehj","onMessageAppReceived===msg-----"+msg);
//			}
//
//			@Override
//			public void onMessageInstantReceive(String account, int uid, String msg) {
//				super.onMessageInstantReceive(account, uid, msg);
//				Logger.d("yehj","onMessageInstantReceive===msg-----"+msg);
//			}
//
//			@Override
//			public void onMessageChannelReceive(String channelID, String account, int uid, String msg) {
//				super.onMessageChannelReceive(channelID, account, uid, msg);
//				Logger.d("yehj","onMessageChannelReceive===msg-----"+msg);
//			}
//
//			@Override
//			public void onLog(String txt) {
//				super.onLog(txt);
//				Logger.d("yehj","onLog=="+txt);
//			}
//
//			@Override
//			public void onInvokeRet(String name, int ofu, String reason, String resp) {
//				super.onInvokeRet(name, ofu, reason, resp);
//				Logger.d("yehj","onInvokeRet");
//			}
//
//			@Override
//			public void onMsg(String from, String t, String msg) {
//				super.onMsg(from, t, msg);
//				Logger.d("yehj","onMsg");
//			}
//
//			@Override
//			public void onUserAttrResult(String account, String name, String value) {
//				super.onUserAttrResult(account, name, value);
//				Logger.d("yehj","onUserAttrResult");
//			}
//
//			@Override
//			public void onUserAttrAllResult(String account, String value) {
//				super.onUserAttrAllResult(account, value);
//				Logger.d("yehj","onUserAttrAllResult");
//			}
//
//			@Override
//			public void onError(String name, int ecode, String desc) {
//				super.onError(name, ecode, desc);
//				Logger.d("yehj","onError");
//			}
//		});
	}

	public String calcToken(String vendorKey, String signKey, String account, long expiredTime){
		// Token = 1:vendorKey:expiredTime:sign
		// Token = 1:vendorKey:expiredTime:md5(account + vendorID + signKey + expiredTime)

		String sign = md5hex((account + vendorKey + signKey + expiredTime).getBytes());
		return "1:" + vendorKey + ":" + expiredTime + ":" + sign;

	}
	public static String md5hex(byte[] s){
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(s);
			return hexlify(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
	public static String hexlify(byte[] data){
		char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
				'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

		/**
		 * 用于建立十六进制字符的输出的大写字符数组
		 */
		char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
				'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

		char[] toDigits = DIGITS_LOWER;
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return String.valueOf(out);

	}
}
