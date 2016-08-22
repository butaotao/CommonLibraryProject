package com.dachen.imsdk.vchat.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.activities.ChatActivityV2;
import com.dachen.imsdk.activities.ImBaseActivity;
import com.dachen.imsdk.consts.EventType;
import com.dachen.imsdk.consts.MessageType;
import com.dachen.imsdk.db.po.ChatMessagePo;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.entity.VChatCallerCancelParam;
import com.dachen.imsdk.net.EventSender;
import com.dachen.imsdk.net.MessageSenderV2;
import com.dachen.imsdk.utils.ImUtils;
import com.dachen.imsdk.vchat.DemoConstants;
import com.dachen.imsdk.vchat.ImVideo;
import com.dachen.imsdk.vchat.MemberInfo;
import com.dachen.imsdk.vchat.R;
import com.dachen.imsdk.vchat.VChatManager;
import com.dachen.imsdk.vchat.VChatManager.VChatLayoutEvent;
import com.dachen.imsdk.vchat.VChatManager.VChatUserEmptyEvent;
import com.dachen.imsdk.vchat.VChatUtil;
import com.dachen.imsdk.vchat.control.AVUIControl;
import com.dachen.imsdk.vchat.control.GLVideoView;
import com.dachen.imsdk.vchat.control.QavsdkControl;
import com.dachen.imsdk.vchat.widget.MyCheckable;
import com.dachen.imsdk.vchat.work.ExternalCaptureThread;
import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.av.sdk.AVConstants;
import com.tencent.av.sdk.AVError;
import com.tencent.av.sdk.AVView;
import com.tencent.av.utils.PhoneStatusTools;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot1.event.EventBus;

public class VChatActivity extends ImBaseActivity implements OnClickListener {
    private static final int MSG_WHAT_UPDATE_TIME = 2009;
    private static final int MSG_HIDE_GUIDE = 2010;
    private static VChatActivity instance;
    private static final String TAG = "VChatActivity";
    private static final int DIALOG_INIT = 0;
    private static final int DIALOG_AT_ON_CAMERA = DIALOG_INIT + 1;
    private static final int DIALOG_ON_CAMERA_FAILED = DIALOG_AT_ON_CAMERA + 1;
    private static final int DIALOG_AT_OFF_CAMERA = DIALOG_ON_CAMERA_FAILED + 1;
    private static final int DIALOG_OFF_CAMERA_FAILED = DIALOG_AT_OFF_CAMERA + 1;
    private static final int DIALOG_AT_SWITCH_FRONT_CAMERA = DIALOG_OFF_CAMERA_FAILED + 1;
    private static final int DIALOG_SWITCH_FRONT_CAMERA_FAILED = DIALOG_AT_SWITCH_FRONT_CAMERA + 1;
    private static final int DIALOG_AT_SWITCH_BACK_CAMERA = DIALOG_SWITCH_FRONT_CAMERA_FAILED + 1;
    private static final int DIALOG_SWITCH_BACK_CAMERA_FAILED = DIALOG_AT_SWITCH_BACK_CAMERA + 1;

    private static final int DIALOG_AT_ON_EXTERNAL_CAPTURE = DIALOG_SWITCH_BACK_CAMERA_FAILED + 1;
    private static final int DIALOG_AT_ON_EXTERNAL_CAPTURE_FAILED = DIALOG_AT_ON_EXTERNAL_CAPTURE + 1;
    private static final int DIALOG_AT_OFF_EXTERNAL_CAPTURE = DIALOG_AT_ON_EXTERNAL_CAPTURE_FAILED + 1;
    private static final int DIALOG_AT_OFF_EXTERNAL_CAPTURE_FAILED = DIALOG_AT_OFF_EXTERNAL_CAPTURE + 1;
    private static final int DIALOG_CHANGE_AUTHRITY_OK = DIALOG_AT_OFF_EXTERNAL_CAPTURE_FAILED + 1;
    private static final int DIALOG_CHANGE_AUTHRITY_FAILED = DIALOG_CHANGE_AUTHRITY_OK + 1;

    private static final int REQ_CODE_ADD_MEMBER = 1001;

    private boolean mIsPaused = false;
    private int mOnOffCameraErrorCode = AVError.AV_OK;
    private int mSwitchCameraErrorCode = AVError.AV_OK;
    private int mEnableExternalCaptureErrorCode = AVError.AV_OK;
    private ProgressDialog mDialogInit = null;
    private ProgressDialog mDialogAtOnCamera = null;
    private ProgressDialog mDialogAtOffCamera = null;

    private ProgressDialog mDialogAtOnExternalCapture = null;
    private ProgressDialog mDialogAtOffExternalCapture = null;

    private ProgressDialog mDialogAtSwitchFrontCamera = null;
    private ProgressDialog mDialogAtSwitchBackCamera = null;
    private QavsdkControl mQavsdkControl;
    private String mRecvIdentifier = "";
    private String mSelfIdentifier = "";
    OrientationEventListener mOrientationEventListener = null;
    int mRotationAngle = 0;
    private static final int TIMER_INTERVAL = 2000; //2s检查一次
    private TextView tvTipsMsg;
    private boolean showTips = false;
    private TextView tvShowTips;
    private Context ctx;

    private ExternalCaptureThread inputStreamThread;
    private boolean isUserRendEnable = false;
    private Button recordButton;
    ;
    private boolean hasHangUp;
    //    private boolean isShowingNewMember;
    private boolean someoneEntered;
    private Button mCtrMute;
    private Button mCtrCamera;
    private Button mCtrSpeaker;
    private boolean mIsFromFloatingWindow;
    private TextView mTimeText;

    private MyHandler mHandler = new MyHandler(this);

    private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int netType = VChatUtil.getNetWorkType(ctx);
            Log.e(TAG, "WL_DEBUG connectionReceiver getNetWorkType = " + netType);
            mQavsdkControl.setNetType(netType);
        }
    };
    private MyCheckable mMuteCheckable = new MyCheckable(true) {
        @Override
        protected void onCheckedChanged(boolean checked) {
//			Button button = (Button) findViewById(R.id.qav_bottombar_mute);
//			AVAudioCtrl avAudioCtrl = mQavsdkControl.getAVContext().getAudioCtrl();
//			if (checked) {
//				button.setSelected(false);
//				avAudioCtrl.enableMic(true);
//			} else {
//				button.setSelected(true);
//				avAudioCtrl.enableMic(false);
//			}

            AVAudioCtrl avAudioCtrl = mQavsdkControl.getAVContext().getAudioCtrl();
            if (checked) {
                mCtrMute.setSelected(false);
                avAudioCtrl.enableMic(true);
                mQavsdkControl.mIsMicEnable = true;
            } else {
                mCtrMute.setSelected(true);
                avAudioCtrl.enableMic(false);
                mQavsdkControl.mIsMicEnable = false;
            }
        }
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {

            runOnUiThread(new Runnable() {
                public void run() {
                    if (showTips) {
                        if (tvTipsMsg != null) {
                            String strTips = mQavsdkControl.getQualityTips();
                            if (!TextUtils.isEmpty(strTips)) {
                                tvTipsMsg.setText(strTips);
                            }
                        }
                    } else {
                        tvTipsMsg.setText("");
                    }
                }
            });
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "WL_DEBUG onReceive action = " + action);
            if (action.equals(VChatUtil.ACTION_SURFACE_CREATED)) {
                locateCameraPreview();
                boolean isEnable = mQavsdkControl.getIsEnableCamera();
                if (isEnable) {
                    showLocalCamera();
                } else{
                    initGuide();
                    openCamera();
                }
                AVAudioCtrl avAudioCtrl = mQavsdkControl.getAVContext().getAudioCtrl();
                avAudioCtrl.enableMic(true);
                mQavsdkControl.enableSpeaker(true);
                refreshSpeakerUI();
                mQavsdkControl.requestAllMember();
            } else if (action.equals(VChatUtil.ACTION_VIDEO_CLOSE)) {
                String identifier = intent.getStringExtra(VChatUtil.EXTRA_IDENTIFIER);
                int videoSrcType = intent.getIntExtra(VChatUtil.EXTRA_VIDEO_SRC_TYPE, AVView.VIDEO_SRC_TYPE_NONE);
                mRecvIdentifier = identifier;
                if (!TextUtils.isEmpty(mRecvIdentifier) && videoSrcType != AVView.VIDEO_SRC_TYPE_NONE) {
                    mQavsdkControl.setRemoteHasVideo(false, mRecvIdentifier, videoSrcType);
                }

            } else if (action.equals(VChatUtil.ACTION_VIDEO_SHOW)) {
                String identifier = intent.getStringExtra(VChatUtil.EXTRA_IDENTIFIER);
                int videoSrcType = intent.getIntExtra(VChatUtil.EXTRA_VIDEO_SRC_TYPE, AVView.VIDEO_SRC_TYPE_NONE);
                mRecvIdentifier = identifier;
                if (!TextUtils.isEmpty(mRecvIdentifier) && videoSrcType != AVView.VIDEO_SRC_TYPE_NONE) {
                    mQavsdkControl.setRemoteHasVideo(true, mRecvIdentifier, videoSrcType);
                }
            } else if (action.equals(VChatUtil.ACTION_ENABLE_CAMERA_COMPLETE)) {
                refreshCameraUI();
                mOnOffCameraErrorCode = intent.getIntExtra(VChatUtil.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);
                boolean isEnable = intent.getBooleanExtra(VChatUtil.EXTRA_IS_ENABLE, false);
                mCtrCamera.setSelected(isEnable);

                if (mOnOffCameraErrorCode == AVError.AV_OK) {
                    mQavsdkControl.setSelfId(mSelfIdentifier);
                    mQavsdkControl.setLocalHasVideo(isEnable, mSelfIdentifier);
                } else {
                    showDialog(isEnable ? DIALOG_ON_CAMERA_FAILED : DIALOG_OFF_CAMERA_FAILED);
                }
                //开启渲染回调的接口
                //mQavsdkControl.setRenderCallback();
            } else if (action.equals(VChatUtil.ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE)) {
                refreshCameraUI();
                mOnOffCameraErrorCode = intent.getIntExtra(VChatUtil.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);
                boolean isEnable = intent.getBooleanExtra(VChatUtil.EXTRA_IS_ENABLE, false);

                if (mOnOffCameraErrorCode == AVError.AV_OK) {
                    //打开外部摄像头之后就开始传输，用户可以实现自己的逻辑
                    //test

                    if (isEnable) {
                        inputStreamThread = new ExternalCaptureThread(getApplicationContext());
                        inputStreamThread.start();
                    } else {
                        if (inputStreamThread != null) {
                            inputStreamThread.canRun = false;
                            inputStreamThread = null;
                        }
                    }
                } else {
                    showDialog(isEnable ? DIALOG_AT_ON_EXTERNAL_CAPTURE_FAILED : DIALOG_AT_OFF_EXTERNAL_CAPTURE_FAILED);
                }
            } else if (action.equals(VChatUtil.ACTION_SWITCH_CAMERA_COMPLETE)) {
                refreshCameraUI();

                mSwitchCameraErrorCode = intent.getIntExtra(VChatUtil.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);
                boolean isFront = intent.getBooleanExtra(VChatUtil.EXTRA_IS_FRONT, false);
                if (mSwitchCameraErrorCode != AVError.AV_OK) {
//					showDialog(isFront ? DIALOG_SWITCH_FRONT_CAMERA_FAILED : DIALOG_SWITCH_BACK_CAMERA_FAILED);
                } else {
                    AVUIControl ui = mQavsdkControl.getAVUIControl();
                    GLVideoView v = ui.getMyselfView();
                    if (v != null) {
                        v.setMirror(isFront);
                    }
                }
            } else if (action.equals(VChatUtil.ACTION_MEMBER_CHANGE)) {
                mQavsdkControl.requestAllMember();
                mQavsdkControl.onMemberChange();
            } else if (action.equals(VChatUtil.ACTION_CHANGE_AUTHRITY)) {
                int result = intent.getIntExtra(VChatUtil.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);
                if (result == AVError.AV_OK) {
                    showDialog(DIALOG_CHANGE_AUTHRITY_OK);
                } else {
                    showDialog(DIALOG_CHANGE_AUTHRITY_FAILED);
                }
            } else if (action.equals(VChatUtil.ACTION_OUTPUT_MODE_CHANGE)) {
                updateHandfreeButton();
            } else if (action.equals(VChatUtil.ACTION_CLOSE_ROOM_COMPLETE)) {
                if (isFinishing()) {
                    return;
                } else {
                    VChatActivity.this.setResult(DemoConstants.AUTO_EXIT_ROOM);
                    finish();
                }
            }
        }
    };

    private void showLocalCamera() {
        if (!mIsPaused) {
            mQavsdkControl.setSelfId(mSelfIdentifier);
            mQavsdkControl.setLocalHasVideo(true, mSelfIdentifier);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "WL_DEBUG onCreate start");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);
        instance = this;
        ctx = this;
        setContentView(R.layout.vchat_activity);
        EventBus.getDefault().register(this);
        //getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | LayoutParams.FLAG_DISMISS_KEYGUARD | LayoutParams.FLAG_KEEP_SCREEN_ON
                | LayoutParams.FLAG_TURN_SCREEN_ON);
        mIsFromFloatingWindow = getIntent().getBooleanExtra("from", false);
        VChatManager.getInstance().isInChat = true;
//		findViewById(R.id.qav_bottombar_handfree).setOnClickListener(this);
        //findViewById(R.id.qav_bottombar_mute).setOnClickListener(this);
        mCtrMute = (Button) findViewById(R.id.qav_bottombar_mute);
        mCtrMute.setOnClickListener(this);
        mCtrCamera = (Button) findViewById(R.id.qav_bottombar_camera);
        mCtrCamera.setOnClickListener(this);
        findViewById(R.id.qav_bottombar_hangup).setOnClickListener(this);
        findViewById(R.id.hide).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
        mCtrSpeaker= (Button) findViewById(R.id.qav_bottombar_speaker);
        mCtrSpeaker.setOnClickListener(this);
        mTimeText = (TextView) findViewById(R.id.time);

        tvTipsMsg = (TextView) findViewById(R.id.qav_tips_msg);
        tvTipsMsg.setTextColor(Color.RED);
        tvShowTips = (TextView) findViewById(R.id.qav_show_tips);
        tvShowTips.setTextColor(Color.GREEN);
        tvShowTips.setText(R.string.tips_show);
        tvShowTips.setOnClickListener(this);
        timer.schedule(task, TIMER_INTERVAL, TIMER_INTERVAL);
        // 注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VChatUtil.ACTION_SURFACE_CREATED);
        intentFilter.addAction(VChatUtil.ACTION_VIDEO_SHOW);
        intentFilter.addAction(VChatUtil.ACTION_VIDEO_CLOSE);
        intentFilter.addAction(VChatUtil.ACTION_ENABLE_CAMERA_COMPLETE);
        intentFilter.addAction(VChatUtil.ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE);
        intentFilter.addAction(VChatUtil.ACTION_SWITCH_CAMERA_COMPLETE);
        intentFilter.addAction(VChatUtil.ACTION_MEMBER_CHANGE);
        intentFilter.addAction(VChatUtil.ACTION_OUTPUT_MODE_CHANGE);
        registerReceiver(mBroadcastReceiver, intentFilter);

        IntentFilter netIntentFilter = new IntentFilter();
        netIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, netIntentFilter);
        mQavsdkControl = ImVideo.getInstance().mQavsdkControl;
        int netType = VChatUtil.getNetWorkType(ctx);
        Log.e(TAG, "WL_DEBUG connectionReceiver onCreate = " + netType);
        if (netType != AVConstants.NETTYPE_NONE) {
            mQavsdkControl.setNetType(VChatUtil.getNetWorkType(ctx));
        }
        mRecvIdentifier = getIntent().getExtras().getString(VChatUtil.EXTRA_IDENTIFIER);
        mSelfIdentifier = getIntent().getExtras().getString(VChatUtil.EXTRA_SELF_IDENTIFIER);
        if (mQavsdkControl.getAVContext() != null) {
            mQavsdkControl.onCreate(getApplicationContext(), findViewById(android.R.id.content));
            updateHandfreeButton();
        } else {
            finish();
        }
        registerOrientationListener();
        refreshInviteList();
//		openCamera();

        if (mIsFromFloatingWindow) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mQavsdkControl.getAVContext().getAudioCtrl().enableMic(mQavsdkControl.mIsMicEnable);
                    mCtrMute.setSelected(!mQavsdkControl.mIsMicEnable);

                }
            });
        }

        updateTime();
//        initGuide();
    }

    public static VChatActivity getInstance() {
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsPaused = false;
        mQavsdkControl.onResume();
        refreshCameraUI();
        if (mOnOffCameraErrorCode != AVError.AV_OK) {
            showDialog(DIALOG_ON_CAMERA_FAILED);
        }
//		startOrientationListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPaused = true;
        mQavsdkControl.onPause();
        refreshCameraUI();
        if (mOnOffCameraErrorCode != AVError.AV_OK) {
            showDialog(DIALOG_OFF_CAMERA_FAILED);
        }
//        stopOrientationListener();
    }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.e("memoryLeak", "memoryLeak avactivity onDestroy");
        // 注销广播
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
        if (timer != null) {
            task.cancel();
            timer.cancel();
            task = null;
            timer = null;
        }
        Log.e("memoryLeak", "memoryLeak avactivity onDestroy end");
        Log.d(TAG, "WL_DEBUG onDestroy");

        if (inputStreamThread != null) {
            inputStreamThread.canRun = false;
            inputStreamThread = null;
        }
        if (hasHangUp) {
            mQavsdkControl.onDestroyExitRoom();
            mQavsdkControl.stopContext();
        } else {
            mQavsdkControl.onDestroy();
        }
    }

    private void sendTextMessage(String content) {
        ChatMessagePo chatMessage = new ChatMessagePo();
        chatMessage.type = MessageType.text;
        chatMessage.content = content;
        chatMessage.fromUserId = ImSdk.getInstance().userId;
        chatMessage.groupId = VChatManager.getInstance().curGroupId;
        MessageSenderV2.getInstance(this).sendMessage(chatMessage);

    }

    public void refreshInviteList() {
        List<UserInfo> m = VChatManager.getInstance().myInviteList;
        if (m.size() == 0) {
            findViewById(R.id.ll_invite_list).setVisibility(View.GONE);
            return;
        }
        findViewById(R.id.ll_invite_list).setVisibility(View.VISIBLE);
        TextView tv = (TextView) findViewById(R.id.tv_invite_list);
        StringBuilder b = new StringBuilder();
        for (UserInfo info : m) {
            b.append(" " + info.name + " ");
        }
        if (tv.getText().toString().equals(b.toString())) {
            return;
        }
        tv.setText(b);
    }

    public void showMemberIn(String userName) {
        someoneEntered = true;
        showMyToast(userName + "已加入视频聊天");
    }

    public void showMemberReject(String userName) {
        showMyToast(userName + "拒绝视频请求");
    }

    public void showMyToast(String text) {
        final TextView tv = new TextView(this);
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setText(text);
        final LinearLayout ll = (LinearLayout) findViewById(R.id.ll_member_in);
        ll.addView(tv);
        tv.postDelayed(new Runnable() {
            @Override
            public void run() {
                ll.removeView(tv);
            }
        }, 2000);
    }

    private void locateCameraPreview() {
//		SurfaceView localVideo = (SurfaceView) findViewById(R.id.av_video_surfaceView);
//		MarginLayoutParams params = (MarginLayoutParams) localVideo.getLayoutParams();
//		params.leftMargin = -3000;
//		localVideo.setLayoutParams(params);

        if (mDialogInit != null && mDialogInit.isShowing()) {
            mDialogInit.dismiss();
        }
    }

    private void openCamera() {
        boolean isEnable = mQavsdkControl.getIsEnableCamera();
        if (isEnable)
            return;
        mOnOffCameraErrorCode = mQavsdkControl.toggleEnableCamera();
        if (mOnOffCameraErrorCode != AVError.AV_OK) {
            showDialog(isEnable ? DIALOG_OFF_CAMERA_FAILED : DIALOG_ON_CAMERA_FAILED);
            mQavsdkControl.setIsInOnOffCamera(false);
        }
        refreshCameraUI();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
//		if (i == R.id.qav_bottombar_handfree) {
//			mQavsdkControl.getAVContext().getAudioCtrl().setAudioOutputMode(mQavsdkControl.getHandfreeChecked() ? AVAudioCtrl.OUTPUT_MODE_SPEAKER : AVAudioCtrl.OUTPUT_MODE_HEADSET);
//		} else
        if (i == R.id.qav_bottombar_mute) {
            mMuteCheckable.toggle();
		} else if (i == R.id.qav_bottombar_camera) {
			boolean isEnable = mQavsdkControl.getIsEnableCamera();
			mOnOffCameraErrorCode = mQavsdkControl.toggleEnableCamera();
			refreshCameraUI();
			if (mOnOffCameraErrorCode != AVError.AV_OK) {
				showDialog(isEnable ? DIALOG_OFF_CAMERA_FAILED : DIALOG_ON_CAMERA_FAILED);
				mQavsdkControl.setIsInOnOffCamera(false);
				refreshCameraUI();
			}
        } else if (i == R.id.qav_bottombar_speaker) {
//            mQavsdkControl.toggleSwitchCamera();
            mQavsdkControl.enableSpeaker(!mQavsdkControl.mIsSpeakerEnable);
            refreshSpeakerUI();
        } else if (i == R.id.qav_bottombar_hangup) {
            hangUp();
        } else if (i == R.id.qav_show_tips) {
            showTips = !showTips;
            if (showTips) {
                tvShowTips.setText(R.string.tips_close);
            } else {
                tvShowTips.setText(R.string.tips_show);
            }
        } else if (i == R.id.hide) {
            onBackPressed();
        } else if (i == R.id.add) {
            Intent intent = new Intent(this, VChatMemberActivity.class);
            intent.putExtra(ChatActivityV2.INTENT_EXTRA_GROUP_ID, VChatManager.getInstance().curGroupId);
            intent.putExtra(VChatMemberActivity.INTENT_ROOM_ID, VChatManager.getInstance().curRoomId);
            startActivityForResult(intent, REQ_CODE_ADD_MEMBER);
        }else if(i==R.id.v_guide_cover){
            hideGuide();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;

        switch (id) {
            case DIALOG_INIT:
                dialog = mDialogInit = VChatUtil.newProgressDialog(this, R.string.interface_initialization);
                break;
            case DIALOG_AT_ON_CAMERA:
                dialog = mDialogAtOnCamera = VChatUtil.newProgressDialog(this, R.string.at_on_camera);
                break;
            case DIALOG_ON_CAMERA_FAILED:
                dialog = VChatUtil.newErrorDialog(this, R.string.on_camera_failed);
                break;
            case DIALOG_AT_OFF_CAMERA:
                dialog = mDialogAtOffCamera = VChatUtil.newProgressDialog(this, R.string.at_off_camera);
                break;
            case DIALOG_OFF_CAMERA_FAILED:
                dialog = VChatUtil.newErrorDialog(this, R.string.off_camera_failed);
                break;

            case DIALOG_AT_ON_EXTERNAL_CAPTURE:
                dialog = mDialogAtOnExternalCapture = VChatUtil.newProgressDialog(this, R.string.at_on_external_capture);
                break;
            case DIALOG_AT_ON_EXTERNAL_CAPTURE_FAILED:
                dialog = VChatUtil.newErrorDialog(this, R.string.on_external_capture_failed);
                break;
            case DIALOG_AT_OFF_EXTERNAL_CAPTURE:
                dialog = mDialogAtOffExternalCapture = VChatUtil.newProgressDialog(this, R.string.at_off_external_capture);
                break;
            case DIALOG_AT_OFF_EXTERNAL_CAPTURE_FAILED:
                dialog = VChatUtil.newErrorDialog(this, R.string.off_external_capture_failed);
                break;

            case DIALOG_AT_SWITCH_FRONT_CAMERA:
                dialog = mDialogAtSwitchFrontCamera = VChatUtil.newProgressDialog(this, R.string.at_switch_front_camera);
                break;
            case DIALOG_SWITCH_FRONT_CAMERA_FAILED:
                dialog = VChatUtil.newErrorDialog(this, R.string.switch_front_camera_failed);
                break;
            case DIALOG_AT_SWITCH_BACK_CAMERA:
                dialog = mDialogAtSwitchBackCamera = VChatUtil.newProgressDialog(this, R.string.at_switch_back_camera);
                break;
            case DIALOG_SWITCH_BACK_CAMERA_FAILED:
                dialog = VChatUtil.newErrorDialog(this, R.string.switch_back_camera_failed);
                break;

            default:
                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_ON_CAMERA_FAILED:
            case DIALOG_OFF_CAMERA_FAILED:
                ((AlertDialog) dialog).setMessage(getString(R.string.error_code_prefix) + mOnOffCameraErrorCode);
                break;
            case DIALOG_SWITCH_FRONT_CAMERA_FAILED:
            case DIALOG_SWITCH_BACK_CAMERA_FAILED:
                ((AlertDialog) dialog).setMessage(getString(R.string.error_code_prefix) + mSwitchCameraErrorCode);
                break;

            case DIALOG_AT_ON_EXTERNAL_CAPTURE_FAILED:
            case DIALOG_AT_OFF_EXTERNAL_CAPTURE_FAILED:
                ((AlertDialog) dialog).setMessage(getString(R.string.error_code_prefix) + mEnableExternalCaptureErrorCode);
                break;

            default:
                break;
        }
    }

    private void refreshCameraUI() {
    }
    private void refreshSpeakerUI() {
        mCtrSpeaker.setSelected(mQavsdkControl.mIsSpeakerEnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stream_set, menu);
        return true;
    }


    private void updateHandfreeButton() {
    }

    class VideoOrientationEventListener extends OrientationEventListener {

        boolean mbIsTablet = false;

        public VideoOrientationEventListener(Context context, int rate) {
            super(context, rate);
            mbIsTablet = PhoneStatusTools.isTablet(context);
        }

        int mLastOrientation = -25;

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                if (mLastOrientation != orientation) {
                    /*
                     * if (mControlUI != null) { mControlUI.setRotation(270); }
					 * if (mVideoLayerUI != null) {
					 * mVideoLayerUI.setRotation(270); }
					 */
                }
                mLastOrientation = orientation;
                return;
            }

            if (mLastOrientation < 0) {
                mLastOrientation = 0;
            }

            if (((orientation - mLastOrientation) < 20)
                    && ((orientation - mLastOrientation) > -20)) {
                return;
            }


            if (mbIsTablet) {
                orientation -= 90;
                if (orientation < 0) {
                    orientation += 360;
                }
            }

            mLastOrientation = orientation;

            if (orientation > 314 || orientation < 45) {
                if (mQavsdkControl != null) {
                    mQavsdkControl.setRotation(0);

                }

                mRotationAngle = 0;
            } else if (orientation > 44 && orientation < 135) {
                if (mQavsdkControl != null) {
                    mQavsdkControl.setRotation(90);
                }
                mRotationAngle = 90;
            } else if (orientation > 134 && orientation < 225) {
                if (mQavsdkControl != null) {
                    mQavsdkControl.setRotation(180);
                }
                mRotationAngle = 180;
            } else {
                if (mQavsdkControl != null) {
                    mQavsdkControl.setRotation(270);
                }
                mRotationAngle = 270;
            }
        }
    }

    void registerOrientationListener() {
        if (mOrientationEventListener == null) {
            mOrientationEventListener = new VideoOrientationEventListener(super.getApplicationContext(), SensorManager.SENSOR_DELAY_UI);
        }
    }

    void startOrientationListener() {
        if (mOrientationEventListener != null) {
            mOrientationEventListener.enable();
        }
    }

    void stopOrientationListener() {
        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_ADD_MEMBER) {
            if (resultCode != RESULT_OK) return;
            refreshInviteList();
        }
    }

    private void hangUp() {
        hasHangUp = true;
        VChatManager man = VChatManager.getInstance();
		/*if(ImUtils.getLoginUserId().equals(man.callerId) &&!someoneEntered){
			String toId="";
			for(UserInfo u:man.myInviteList){
				toId+=u.id+"|";
			}
			VChatCallerCancelParam p=new VChatCallerCancelParam();
			p.fromUSerId= ImUtils.getLoginUserId();
			p.gid=man.curGroupId;
			p.roomId=man.curRoomId;
			Map<String,String> map=new HashMap<>();
			map.put("invite", JSON.toJSONString(p));
			EventSender.getInstance(this).sendEvent(EventType.V_CHAT_CALLER_CANCEL,toId,map);
		}*/


        List<MemberInfo> memberList = ImVideo.getInstance().mQavsdkControl.getMemberList();

        if (memberList != null && memberList.size() == 1) {
            String toId = "";
            for (UserInfo u : VChatManager.getInstance().allUserList) {
                if (ImUtils.getLoginUserId().equals(u.id)) {
                    continue;
                }
                toId += u.id + "|";
            }
            if(memberList.size()==1){
                //最后一个退出视频通话的人发送通话结束消息
                sendTextMessage("本次视频通话已结束");
                //当最后一个人退出房间时，对于已经收到邀请但还没有进来的人需要取消呼叫
                exitCall(toId,EventType.V_CHAT_CALLER_CANCEL);
            }else{
                exitCall(toId,EventType.V_CHAT_EXIT);
            }
        }


        man.clearRoom();
        finish();
    }

    /**
     * 取消呼叫
     */
    private void exitCall(String toIds,int eventId) {
        if(TextUtils.isEmpty(toIds)){
            return;
        }

        VChatCallerCancelParam p = new VChatCallerCancelParam();
        p.fromUSerId = ImUtils.getLoginUserId();
        p.gid = VChatManager.getInstance().curGroupId;
        p.roomId = VChatManager.getInstance().curRoomId;
        Map<String, String> map = new HashMap<>();
        map.put("invite", JSON.toJSONString(p));
        EventSender.getInstance(this).sendEvent(eventId, toIds, map);
    }

    private void updateTime() {
        mHandler.removeMessages(MSG_WHAT_UPDATE_TIME);
        long start = VChatManager.getInstance().startTime;
        if (start == 0) {
            mTimeText.setText("00:00");
        } else {
            long timeMils = System.currentTimeMillis() - start;
            int secTotal = (int) (timeMils / 1000);
            int min = secTotal / 60;
            int sec = secTotal % 60;
            String str = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
            mTimeText.setText(str);
        }
        mHandler.sendEmptyMessageDelayed(MSG_WHAT_UPDATE_TIME, 1000);
    }

    private void initGuide(){
        findViewById(R.id.v_guide_cover).setOnClickListener(this);
        findViewById(R.id.v_guide_cover).setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_GUIDE,3000);
    }
    private void hideGuide(){
        findViewById(R.id.v_guide_cover).setVisibility(View.GONE);
    }

    private static class MyHandler extends Handler {
        WeakReference<VChatActivity> mActivityWeakReference;

        public MyHandler(VChatActivity activity) {
            this.mActivityWeakReference = new WeakReference<VChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VChatActivity activity = mActivityWeakReference.get();
            switch (msg.what) {
                case MSG_WHAT_UPDATE_TIME:
                    activity.updateTime();
                    break;
                case MSG_HIDE_GUIDE:
                    activity.hideGuide();
                    break;
            }
        }
    }
    public void onEventMainThread(VChatLayoutEvent e){
        AVUIControl control=mQavsdkControl.getAVUIControl();
        if(control.enlargeUserId==null){
            findViewById(R.id.v_btn_container).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.v_btn_container).setVisibility(View.INVISIBLE);
        }
    }
    public void onEventMainThread(VChatUserEmptyEvent e){
        hangUp();
    }
}