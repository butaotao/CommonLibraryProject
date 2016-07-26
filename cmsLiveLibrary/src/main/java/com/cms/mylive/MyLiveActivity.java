package com.cms.mylive;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.mylive.adapter.ViewPagerAdapter;
import com.cms.mylive.bean.Meeting;
import com.cms.mylive.callbacks.MyCallback;
import com.cms.mylive.callbacks.MyIAudioCallBack;
import com.cms.mylive.callbacks.MyIRoomCallBack;
import com.cms.mylive.callbacks.MyIVideoCallBack;
import com.cms.mylive.fragment.DocFragment;
import com.cms.mylive.fragment.FragmentFactory;
import com.cms.mylive.utils.ProgressDialogUtil;
import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.net.AbsRtAction;
import com.gensee.net.RtComp;
import com.gensee.room.RtSdk;
import com.gensee.routine.IRTEvent;
import com.gensee.routine.UserInfo;
import com.gensee.rtlib.ChatResource;
import com.gensee.taskret.OnTaskRet;
import com.gensee.view.GSVideoView;
import com.gensee.view.GSVideoView.RenderMode;
import com.gensee.view.LocalVideoView;

import java.util.ArrayList;
import java.util.List;

public class MyLiveActivity extends FragmentActivity {
    private static final String TAG = MyLiveActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private GSVideoView mGSVideoView;
    private RadioGroup mRadioGroup;
    private RelativeLayout rlNoVideo;

    private RtSdk mRtSdk;
    //public static RtSdk mStaticRtSdk;
    public String initParam;
    private long activeId = 0;
    private ViewPagerAdapter adapter;
    private List<Fragment> lists;
    private ProgressDialog mProgressDialog;
    private UserInfo self;
    private static final int OFF_SCREEN_PAGE_LIMIT = 3;
    private static final int JOIN_RESUTL = 1314;
    public static final int JOIN_OK = JOIN_RESUTL + 1;
    public static final int JOIN_FAILURE = JOIN_RESUTL + 2;
    private int mCurrentPageIndex = 0;
    public static final int RE_CONNECTIN = JOIN_RESUTL + 3;
    public static final int VIDEO_JOIN_SUCESS = JOIN_RESUTL + 4;
    public static final int INIT_ERROR = JOIN_RESUTL + 5;
    public static final int USER_EXIT = JOIN_RESUTL + 6;
    public static final int RELEASE_FINISHED = JOIN_RESUTL + 7;
    private Meeting mMeeting;
    private TextView mBack;
    private LocalVideoView mLocalVideoView;
    private FrameLayout mFrameLayout;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int result = msg.what;
            switch (result) {
                case JOIN_FAILURE:
                    showJoinErrorMessage(msg.arg1);
                    release(1000);
                    break;
                case JOIN_OK:
                    dismissDialog();
                    break;
                case RE_CONNECTIN:
                    // dismissDialog();
                    // mProgressDialog.show(MainActivity.this, null, "正在重新连接...",
                    // false,
                    // false);
                    break;
                case VIDEO_JOIN_SUCESS:
                    dismissDialog();
                    rlNoVideo.setVisibility(View.GONE);
                    break;
                case INIT_ERROR:
                    //LogUtils.getInstance().toast(getApplicationContext(), "初始化异常");
                    showInitErrorMessage(msg.arg1);
                    release(1000);
                    break;
                case USER_EXIT:
                    releaseRtSDK();
                    break;
                case RELEASE_FINISHED:
                    release();
                    break;
            }
        }

        ;
    };

    private void releaseRtSDK() {
        if (mRtSdk != null) {
            mRtSdk.release(new OnTaskRet() {
                @Override
                public void onTaskRet(boolean b, int i, String s) {
                    if (b) {
                        Message msg = handler.obtainMessage();
                        msg.what = RELEASE_FINISHED;
                        msg.sendToTarget();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mylive);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initView();
        initViewPager();
        initRadioGroup();
        initJoinParameters();
        initMeeting();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mGSVideoView = (GSVideoView) findViewById(R.id.videoCasting);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg);
        rlNoVideo = (RelativeLayout) findViewById(R.id.showNoVideo);
        mLocalVideoView = (LocalVideoView) findViewById(R.id.localVideoView);
        mLocalVideoView.setOrientation(Configuration.ORIENTATION_PORTRAIT);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitMeeting();
            }
        });
    }

    private void initJoinParameters() {
        try {
            Bundle bundle = getIntent().getBundleExtra("joinMeeting");
            mMeeting = new Meeting();
            if (bundle != null) {
                mMeeting.domain = bundle.getString("domain");
                mMeeting.number = bundle.getString("number");
                mMeeting.loginAccount = bundle.getString("loginAccount");
                mMeeting.loginPwd = bundle.getString("loginPwd");
                mMeeting.nickName = bundle.getString("nickName");
                mMeeting.joinPwd = bundle.getString("joinPwd");

                Intent intent = new Intent();
                this.setResult(RESULT_OK, intent);
            }
        } catch (Exception e) {
            Intent intent = new Intent();
            this.setResult(RESULT_CANCELED, intent);
            this.finish();
        }

    }

    private void initViewPager() {
        lists = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Fragment mFragment = FragmentFactory.createFragment(i);
            lists.add(mFragment);
        }

        FragmentManager fm = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(fm, lists);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(OFF_SCREEN_PAGE_LIMIT);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
    }

    private void initRadioGroup() {
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    // 滑动事件的监听
    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int index) {
            // 滚动下面的菜单栏
            ((RadioButton) mRadioGroup.getChildAt(index)).setChecked(true);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
    // 菜单栏选择监听
    private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id = group.getCheckedRadioButtonId();
            if (id == R.id.btnHome) {
                mCurrentPageIndex = 0;
            } else if (id == R.id.btnChat) {
                mCurrentPageIndex = 1;
            } else if (id == R.id.btnQuestion) {
                mCurrentPageIndex = 2;
            } else if (id == R.id.btnVote) {
                mCurrentPageIndex = 3;
            }
            mViewPager.setCurrentItem(mCurrentPageIndex);
        }
    };

    /**
     * 初始化开会的一些参数
     */
    private void initMeeting() {

        mProgressDialog = ProgressDialogUtil.init(this, null, "正在加入会议中...", false);
        mProgressDialog.show();

        // 初始化资源
        ChatResource.initChatResource(this);
        mRtSdk = new RtSdk();
        //mStaticRtSdk = mRtSdk;

        RtComp mRtComp = new RtComp(MyLiveActivity.this, new CallbackImpl());
        InitParam p = new InitParam();
        // 连接数据
        if (mMeeting != null) {
            p.setDomain(mMeeting.domain);
            p.setNumber(mMeeting.number);
            p.setLoginAccount(mMeeting.loginAccount);
            p.setLoginPwd(mMeeting.loginPwd);
            p.setNickName(mMeeting.nickName);
            p.setJoinPwd(mMeeting.joinPwd);
            p.setServiceType(ServiceType.ST_CASTLINE);

            mRtComp.initWithGensee(p);
        }
    }

    /**
     * 返回rgsdk接口
     *
     * @return
     */
    public RtSdk getRtSdk() {
        if (mRtSdk != null)
            return mRtSdk;
        return null;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public int getPopupWindowHeight() {
        return mViewPager.getHeight() + mRadioGroup.getHeight();
    }

    /**
     * 加入方会议的接口
     *
     * @author Administrator
     */
    private class CallbackImpl extends MyCallback {
        @Override
        public void onErr(int errCode) {
            Log.d(TAG, "errCode=" + errCode);
            // switch (errCode) {
            // case ERR_DOMAIN:
            // toast("domain不正确");
            // break;
            // case ERR_NUMBER_UNEXIST:
            // toast("直播间不存在");
            // break;
            // case ERR_TOKEN:
            // toast("口令错误");
            // break;
            // case ERR_SERVICE:
            // toast("请选择正确服务类型（webcast meeting training）");
            // break;
            // case ERR_UN_NET:
            // toast("请检查网络");
            // break;
            // case ERR_TIME_OUT:
            // toast("连接超时，请重试");
            // break;
            // case ERR_PARAM:
            // toast("initParam 不正确");
            // break;
            // default:
            // toast("初始化错误，错误码：" + errCode + ",请查对");
            // break;
            // }

            Message msg = handler.obtainMessage();
            msg.what = INIT_ERROR;
            msg.arg1 = errCode;
            msg.sendToTarget();
        }

        @Override
        public void onInited(String arg0) {
            initParam = arg0;
            if (mRtSdk != null)
                mRtSdk.initWithParam("", arg0, new IRoomCallBackImpl());
        }
    }

    /**
     * 加入视频会议房间的接口
     *
     * @author Administrator
     */
    private class IRoomCallBackImpl extends MyIRoomCallBack {

        @Override
        public Context onGetContext() {
            return MyLiveActivity.this;
        }

        @Override
        public void onInit(boolean arg0) {
            if (arg0) {
                Log.i("demo", "onInit");
                mRtSdk.setDocView(((DocFragment) lists.get(0)).getGSDocView());
                mRtSdk.setVideoCallBack(new IVideoCallBackImpl());
                mRtSdk.setAudioCallback(new IAudioCallBack());
                mGSVideoView.setRenderMode(RenderMode.RM_FILL_XY);
                mRtSdk.setLocalVideoView(mLocalVideoView);

                mRtSdk.join(new OnTaskRet() {

                    @Override
                    public void onTaskRet(boolean arg0, int arg1, String arg2) {
                        Log.d(TAG, "arg0=" + arg0);
//                        if (arg0)
//                            Toast.makeText(MyLiveActivity.this, "加入成功", 0).show();
//                        else
//                            Toast.makeText(MyLiveActivity.this, "加入失败", 0).show();
                    }
                });
            }
        }

        @Override
        public void onJoin(boolean arg0) {

        }

        @Override
        public void onRoomJoin(int result, UserInfo self) {
            Log.d(TAG, "result=" + result);
//            switch (result) {
//                case JR_OK:
//                    MyLiveActivity.this.self = self;
//                    // toast("加入成功");
//                    break;
//                case JR_ERROR_HOST:
//                    // toast("组织者已经加入（老师已经加入）");
//                    break;
//                case JR_ERROR_GETPARAM:
//                    // toast("加入参数错误");
//                    break;
//                case JR_ERROR_LICENSE:
//                    // toast("人数已满");
//                    break;
//                case JR_ERROR_LOCKED:
//                    // toast("直播间（课堂）被锁定");
//                    break;
//                case JR_ERROR_CODEC:
//                    // toast("音频编码不匹配");
//                    break;
//
//                default:
//                    break;
//            }
            // 加入直播有问题,释放有所的资源
            Message msg = handler.obtainMessage();
            if (result != JR_OK) {
                msg.arg1 = result;
                msg.what = JOIN_FAILURE;
            } else {
                MyLiveActivity.this.self = self;
                msg.what = JOIN_OK;
            }
            msg.sendToTarget();

        }

        // 这里表示用户加入的时候的回调接口
        @Override
        public void onRoomUserJoin(UserInfo arg0) {
            sendBroadcast();
        }

        @Override
        public void onRoomUserLeave(UserInfo arg0) {
            sendBroadcast();
        }

        // 这里表示用户名字更改时候的回调
        @Override
        public void onRoomUserUpdate(UserInfo arg0) {
            sendBroadcast();
        }

        // 离开视频
        @Override
        public void onRoomLeave(int reason) {
            // switch (reason) {
            // case LR_CLOSED:
            // LogUtils.getInstance().toast(MainActivity.this,"直播（课堂）已经关闭");
            // break;
            // case LR_EJECTED:
            // LogUtils.getInstance().toast(MainActivity.this,"被踢出直播（课堂）");
            // break;
            // case LR_TIMESUP:
            // LogUtils.getInstance().toast(MainActivity.this,"超时，直播(课堂已过期)");
            // break;
            // case LR_NORMAL:
            // LogUtils.getInstance().toast(MainActivity.this,"已经退出直播（课堂）");
            // break;
            //
            // default:
            // break;
            // }
            if (reason == LR_NORMAL) {//用户自动退出
                Message msg = handler.obtainMessage();
                msg.what = USER_EXIT;
                msg.sendToTarget();
            } else {
                // 已经退出房间，请进行释放以便于
                Message msg = handler.obtainMessage();
                msg.what = JOIN_FAILURE;
                msg.sendToTarget();
            }
        }

        @Override
        public void onRoomReconnecting() {
            Message msg = handler.obtainMessage();
            msg.what = RE_CONNECTIN;
            msg.sendToTarget();
        }

    }

    // 当用户发生改变的时候，就发出广播
    private void sendBroadcast() {
        // 发送广播
        Intent intent = new Intent();
        intent.setAction("com.cms.FINISH_USERS");
        sendBroadcast(intent);
    }

    /**
     * 视频回调接口
     */
    private class IVideoCallBackImpl extends MyIVideoCallBack {
        @Override
        public void onVideoActived(UserInfo user, boolean bActived) {
            super.onVideoActived(user, bActived);
            if (user == null) {
                return;
            }
            long userId = user.getId();
            if (bActived) {
                // 取消上一个直播视频，tip：如果是要显示多个“个人”的视频，是需要修改的
                if (activeId != 0) {
                    mRtSdk.unDisplayVideo(activeId, null);
                }
                activeId = userId;

                if (userId == self.getId()) {//自己是直播者，为节省流量不需要订阅（显示）自己的视频
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGSVideoView.setVisibility(View.INVISIBLE);//隐藏mGSVideoView，让mLocalVideoView显示出来
                        }
                    });
                } else {
                    // 订阅userid的视频数据
                    mRtSdk.displayVideo(userId, null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mGSVideoView.getVisibility() != View.VISIBLE) {
                                mGSVideoView.setVisibility(View.VISIBLE);//让mGSVideoView显示，遮挡mLocalVideoView
                                mFrameLayout.bringChildToFront(mGSVideoView);

                            }
                        }
                    });
                }
            } else {
                // 取消订阅userid的视频数据
                activeId = 0;
                mRtSdk.unDisplayVideo(userId, null);

                if (userId == self.getId()) {//自己是直播者
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mGSVideoView.getVisibility() != View.VISIBLE) {
                                mGSVideoView.setVisibility(View.VISIBLE);//让mGSVideoView显示，遮挡mLocalVideoView
                                mFrameLayout.bringChildToFront(mGSVideoView);
                            }
                        }
                    });
                }
            }
        }

        @Override
        public void onVideoDataRender(long userId, int width, int height, int frameFormat, float displayRatio,
                                      byte[] data) {
            mGSVideoView.onReceiveFrame(data, width, height);

        }

        @Override
        public void onVideoJoin(UserInfo user) {
            super.onVideoJoin(user);
            if (user == null) {
                return;
            }
            long userId = user.getId();
            // 插播视频
            if (UserInfo.LOD_USER_ID == userId) {
                // isLodPlaying = true;
                mRtSdk.displayVideo(userId, null);
            }
            // 发送视频直播成功的消息
            Message msg = handler.obtainMessage();
            msg.what = VIDEO_JOIN_SUCESS;
            msg.sendToTarget();
        }

        @Override
        public void onVideoLeave(long userId) {
            super.onVideoLeave(userId);
            if (UserInfo.LOD_USER_ID == userId) {
                // isLodPlaying = false;
                mRtSdk.unDisplayVideo(userId, null);
            }
        }

        @Override
        public void onVideoCameraOpened() {
            super.onVideoCameraOpened();
            Log.d(TAG, "onVideoCameraOpened");
            mRtSdk.videoActive(self.getId(), true, null);
        }

        @Override
        public void onVideoCameraClosed() {
            super.onVideoCameraClosed();
            Log.d(TAG, "onVideoCameraClosed");
            mRtSdk.videoActive(self.getId(), false, null);
        }
    }

    /**
     * 音频回调接口
     */
    private class IAudioCallBack extends MyIAudioCallBack {
        @Override
        public Context onGetContext() {

            return MyLiveActivity.this;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        dismissDialog();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitMeeting();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitMeeting() {
        Builder builder = new Builder(MyLiveActivity.this);
        builder.setTitle("退出会议吗？");
        builder.setMessage("真的要退出吗？");
        builder.setPositiveButton("退出", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRtSdk.leave(false, null);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    // 退出,释放资源
    private void release() {
        if (initParam != null)
            initParam = null;
        if (self != null)
            self = null;
        dismissDialog();
        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);
        finish();
    }

    private void release(long delayMillis) {
        if (initParam != null)
            initParam = null;
        if (self != null)
            self = null;
        dismissDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, delayMillis);

    }

    private void showInitErrorMessage(int errCode) {
        switch (errCode) {
            case AbsRtAction.ErrCode.ERR_DOMAIN:
                toast("domain不正确");
                break;
            case AbsRtAction.ErrCode.ERR_NUMBER_UNEXIST:
                toast("直播间不存在");
                break;
            case AbsRtAction.ErrCode.ERR_TOKEN:
                toast("您输入的密码有误");
                break;
            case AbsRtAction.ErrCode.ERR_SERVICE:
                toast("请选择正确服务类型（webcast meeting training）");
                break;
            case AbsRtAction.ErrCode.ERR_UN_NET:
                toast("请检查网络");
                break;
            case AbsRtAction.ErrCode.ERR_TIME_OUT:
                toast("连接超时，请重试");
                break;
            case AbsRtAction.ErrCode.ERR_PARAM:
                toast("initParam 不正确");
                break;
            default:
                toast("初始化错误");
                break;
        }
    }

    private void showJoinErrorMessage(int result) {
        switch (result) {
//            case IRTEvent.IRoomEvent.JoinResult.JR_OK:
//                toast("加入成功");
//                break;
            case IRTEvent.IRoomEvent.JoinResult.JR_ERROR_HOST:
                toast("组织者已经加入");
                break;
            case IRTEvent.IRoomEvent.JoinResult.JR_ERROR_GETPARAM:
                toast("加入参数错误");
                break;
            case IRTEvent.IRoomEvent.JoinResult.JR_ERROR_LICENSE:
                toast("该会议室人数已满");
                break;
            case IRTEvent.IRoomEvent.JoinResult.JR_ERROR_LOCKED:
                toast("直播间被锁定");
                break;
            case IRTEvent.IRoomEvent.JoinResult.JR_ERROR_CODEC:
                toast("音频编码不匹配");
                break;

            default:
                break;
        }
    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
