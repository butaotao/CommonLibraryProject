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
import com.dachen.teleconference.MyAgoraAPICallBack;
import com.dachen.teleconference.MyRtcEngineEventHandler;
import com.dachen.teleconference.R;
import com.dachen.teleconference.bean.GetMediaDynamicKeyResponse;
import com.dachen.teleconference.bean.GetSigningKeyResponse;
import com.dachen.teleconference.common.BaseActivity;
import com.dachen.teleconference.http.HttpCommClient;
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
	private AudioManager mAm;
	private Handler mHandler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case getMediaDynamicKey:
					if(msg.arg1==1){
						if(msg.obj!=null){
							if(requestAudioFocus()){
								AgoraManager.getInstance(AgoraVoiceSdkDemo.this).joinChannel(channel_id.getText().toString(),((GetMediaDynamicKeyResponse)msg.obj).getData(),Integer.parseInt(user_id.getText().toString()));
							}

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
							Logger.d("yehj","token=="+token);
							AgoraManager.getInstance(AgoraVoiceSdkDemo.this).loginAgora(user_id.getText().toString(),((GetSigningKeyResponse)msg.obj).getData(),vendorKey);
						}
					}else{
						UIHelper.ToastMessage(AgoraVoiceSdkDemo.this,(String)msg.obj);
					}
					break;

			}
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
			HttpCommClient.getInstance().getMediaDynamicKey(this,mHandler,getMediaDynamicKey,channel_id.getText().toString(),user_id.getText().toString(),"3600");

		}else if(v.getId() == R.id.btn_leave){

		}else if(v.getId() == R.id.btn_switcher){

		}else if(v.getId() == R.id.btn_ctr2){
			if(TextUtils.isEmpty(user_id.getText().toString())||TextUtils.isEmpty(channel_id.getText().toString())){
				UIHelper.ToastMessage(this,"userid或者channel_id不能为空");
				return;
			}
			if(!isLogin){
				HttpCommClient.getInstance().getSigningKey(this,mHandler,getSignningKey,user_id.getText().toString(),"3600");
			}else{
				AgoraManager.getInstance(AgoraVoiceSdkDemo.this).logoutAgora();
			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initAgoraConfigure(){
		AgoraManager.getInstance(this).initAgora(vendorKey);
		AgoraManager.getInstance(this).getEventHandlerMgr().addRtcEngineEventHandler(new MyRtcEngineEventHandler() {
			@Override
			public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

			}

			@Override
			public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {

			}

			@Override
			public void onWarning(int warn) {

			}

			@Override
			public void onError(int err) {

			}

			@Override
			public void onApiCallExecuted(String api, int error) {

			}

			@Override
			public void onCameraReady() {

			}

			@Override
			public void onVideoStopped() {

			}

			@Override
			public void onAudioQuality(int uid, int quality, short delay, short lost) {

			}

			@Override
			public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {

			}

			@Override
			public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {

			}

			@Override
			public void onAudioVolumeIndication(IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {

			}

			@Override
			public void onNetworkQuality(int quality) {

			}

			@Override
			public void onUserJoined(int uid, int elapsed) {

			}

			@Override
			public void onUserOffline(int uid, int reason) {

			}

			@Override
			public void onUserMuteAudio(int uid, boolean muted) {

			}

			@Override
			public void onUserMuteVideo(int uid, boolean muted) {

			}

			@Override
			public void onUserEnableVideo(int uid, boolean enabled) {

			}

			@Override
			public void onLocalVideoStat(int sentBitrate, int sentFrameRate) {

			}

			@Override
			public void onRemoteVideoStat(int uid, int delay, int receivedBitrate, int receivedFrameRate) {

			}

			@Override
			public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {

			}

			@Override
			public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {

			}

			@Override
			public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {

			}

			@Override
			public void onFirstLocalVideoFrame(int width, int height, int elapsed) {

			}

			@Override
			public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

			}

			@Override
			public void onConnectionLost() {

			}

			@Override
			public void onConnectionInterrupted() {

			}

			@Override
			public void onRefreshRecordingServiceStatus(int status) {

			}
		});
		AgoraManager.getInstance(this).getAgoraAPICallBack().addAgoraAPICallBack(new MyAgoraAPICallBack() {
			@Override
			public void onReconnecting(int nretry) {

			}

			@Override
			public void onReconnected(int fd) {

			}

			@Override
			public void onLoginSuccess(int uid, int fd) {
				btn_ctr2.setText("LogOut");
				isLogin=true;

			}

			@Override
			public void onLogout(int ecode) {
				btn_ctr2.setText("Login");
				isLogin=false;
			}

			@Override
			public void onLoginFailed(int ecode) {

			}

			@Override
			public void onChannelJoined(String channelID) {

			}

			@Override
			public void onChannelJoinFailed(String channelID, int ecode) {

			}

			@Override
			public void onChannelLeaved(String channelID, int ecode) {

			}

			@Override
			public void onChannelUserJoined(String account, int uid) {

			}

			@Override
			public void onChannelUserLeaved(String account, int uid) {

			}

			@Override
			public void onChannelUserList(String[] accounts, int[] uids) {

			}

			@Override
			public void onChannelQueryUserNumResult(String channelID, int ecode, int num) {

			}

			@Override
			public void onChannelAttrUpdated(String channelID, String name, String value, String type) {

			}

			@Override
			public void onInviteReceived(String channelID, String account, int uid) {

			}

			@Override
			public void onInviteReceivedByPeer(String channelID, String account, int uid) {

			}

			@Override
			public void onInviteAcceptedByPeer(String channelID, String account, int uid) {

			}

			@Override
			public void onInviteRefusedByPeer(String channelID, String account, int uid) {

			}

			@Override
			public void onInviteFailed(String channelID, String account, int uid, int ecode) {

			}

			@Override
			public void onInviteEndByPeer(String channelID, String account, int uid) {

			}

			@Override
			public void onInviteEndByMyself(String channelID, String account, int uid) {

			}

			@Override
			public void onMessageSendError(String messageID, int ecode) {

			}

			@Override
			public void onMessageSendSuccess(String messageID) {

			}

			@Override
			public void onMessageAppReceived(String msg) {

			}

			@Override
			public void onMessageInstantReceive(String account, int uid, String msg) {

			}

			@Override
			public void onMessageChannelReceive(String channelID, String account, int uid, String msg) {

			}

			@Override
			public void onLog(String txt) {

			}

			@Override
			public void onInvokeRet(String name, int ofu, String reason, String resp) {

			}

			@Override
			public void onMsg(String from, String t, String msg) {

			}

			@Override
			public void onUserAttrResult(String account, String name, String value) {

			}

			@Override
			public void onUserAttrAllResult(String account, String value) {

			}

			@Override
			public void onError(String name, int ecode, String desc) {

			}
		});
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

	boolean requestAudioFocus() { // can always call requestAudioFocus, if already has it, then result is still AUDIOFOCUS_REQUEST_GRANTED
		// try do request audio focus in audio thread(try to keep the setMode in sequence)
		mAm = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		int result = mAm.requestAudioFocus(afChangeListener,
				AudioManager.STREAM_VOICE_CALL,
				AudioManager.AUDIOFOCUS_GAIN);


		if (result == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
			return false;
		}

		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			//mSystemWideAudioMode = mAm.getMode();
			//mAm.setMode(android.media.AudioManager.MODE_IN_COMMUNICATION);
//            int volume = mAm.getStreamVolume(android.media.AudioManager.STREAM_VOICE_CALL);
//            int maxVolume = mAm.getStreamMaxVolume(android.media.AudioManager.STREAM_VOICE_CALL);
			return true;
		}
		throw new IllegalAccessError("Trespass");
	}
	void abandonAudioFocus() {
		mAm = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		mAm.abandonAudioFocus(afChangeListener);
	}
	AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			// for AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK | AUDIOFOCUS_LOSS_TRANSIENT, we do nothing
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				mAm.abandonAudioFocus(this);
//                sysMessage(BKMessageCode.AUDIO_FOCUS_LOST, null);
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
				//mAm.setMode(android.media.AudioManager.MODE_IN_COMMUNICATION);
			}
		}
	};
}
