package com.dachen.imsdk.vchat.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dachen.common.media.SoundPlayer;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.UIHelper;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.vchat.R;;
import com.dachen.imsdk.activities.ImBaseActivity;
import com.dachen.imsdk.consts.EventType;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.entity.VChatInviteParam;
import com.dachen.imsdk.entity.VChatRejectParam;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.imsdk.net.SessionGroup.SessionGroupCallbackNew;
import com.dachen.imsdk.utils.ImUtils;
import com.dachen.imsdk.vchat.ImVideo;
import com.dachen.imsdk.vchat.VChatManager;
import com.dachen.imsdk.vchat.VChatUtil;
import com.dachen.imsdk.vchat.control.QavsdkControl;
import com.dachen.imsdk.vchat.work.InitVChatTasks;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.av.sdk.AVError;
import com.tencent.av.sdk.AVRoomMulti;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mcp on 2016/3/19.
 */
public class VChatInvitedActivity extends ImBaseActivity implements OnClickListener {
    private String TAG = VChatInvitedActivity.class.getSimpleName();
    private static final int MSG_WHAT_TIMEOUT = 2008;
    private static VChatInvitedActivity instance;

    private final int MAX_NUM = 9;
    public static String INTENT_PARAM = "param";
    private VChatInviteParam mParam;
    private LinearLayout mMemberLayout;
    private int endpointNum = -1;
    private QavsdkControl mQavsdkControl = ImVideo.getInstance().mQavsdkControl;
    private boolean accepted;
    private BroadcastReceiver mBroadcastReceiver;
//    private float oldAudioVol=-1;
    private MyHandler mHandler = new MyHandler(this);
    private SoundPlayer mSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (instance != null) {
            finish();
            return;
        }
        instance = this;
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
        setContentView(R.layout.vchat_invite);
        mDialog.setMessage("正在加入视频聊天");
        mParam = (VChatInviteParam) getIntent().getSerializableExtra(INTENT_PARAM);
        VChatManager man=VChatManager.getInstance();
        man.callerId=mParam.fromUserId;
        man.curRoomId=mParam.roomId;
        findViewById(R.id.receive_video_call).setOnClickListener(this);
        findViewById(R.id.refuse_video_call).setOnClickListener(this);
//        TextView tvName= (TextView) findViewById(R.id.name);
//        tvName.setText(mParam.inviter.name);
//        ImageView iv= (ImageView) findViewById(R.id.head_image);
//        ImageLoader.getInstance().displayImage(mParam.inviter.pic,iv,ImUtils.getAvatarNormalImageOptions());
        regReceiver();
        mMemberLayout = (LinearLayout) findViewById(R.id.ll_member);
        initMembers();
        if (mQavsdkControl.hasAVContext()) {
//            closeVoice();
            mQavsdkControl.enterRoom(mParam.roomId, ImUtils.getLoginUserId());
        } else {
//            ToastUtil.showToast(mThis, "腾讯SDK未启动,重新启动腾讯SDK");
            new InitVChatTasks(ImUtils.getLoginUserId()).execute();
        }

        //如果30s内没有接听，则取消呼叫  --改为60s
        mHandler.sendEmptyMessageDelayed(MSG_WHAT_TIMEOUT, 60 * 1000);

        mSoundPlayer = new SoundPlayer(this);
        mSoundPlayer.play(getSoundUri());
    }

    public static VChatInvitedActivity getInstance() {
        return instance;
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(MSG_WHAT_TIMEOUT);
        instance = null;
        super.onDestroy();
        if (mBroadcastReceiver != null)
            unregisterReceiver(mBroadcastReceiver);
        if (mQavsdkControl.hasAVContext()) {
//            mQavsdkControl.getAVContext().getAudioCtrl().enableSpeaker(true);
            mQavsdkControl.enableSpeaker(true);
        }
        if (mSoundPlayer != null) {
            mSoundPlayer.stop();
            mSoundPlayer.release();
        }
    }

    private Uri getSoundUri() {
        return Uri.parse("android.resource://" + getPackageName() + "/raw/" + "video_call_incoming");
    }
//    private void closeVoice(){
//        AVAudioCtrl ctrl=mQavsdkControl.getAVContext().getAudioCtrl();
//        oldAudioVol=ctrl.getAudioDataVolume(AVAudioCtrl.OUTPUT_MODE_SPEAKER);
//        ctrl.setAudioDataVolume(AVAudioCtrl.OUTPUT_MODE_SPEAKER,0);
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.receive_video_call) {
            if (endpointNum == -1) {
                mDialog.show();
            } else if (endpointNum > MAX_NUM) {
                UIHelper.ToastMessage(mThis, "人数达到上限");
            } else {
                mHandler.removeMessages(MSG_WHAT_TIMEOUT);
                goInChat();
            }
//            if(mQavsdkControl.hasAVContext()) {
//                mQavsdkControl.enterRoom(mParam.roomId, ImUtils.getLoginUserId());
//            } else {
//                ToastUtil.showToast(mThis, "腾讯SDK未启动,重新启动腾讯SDK");
//                new InitVChatTasks(ImUtils.getLoginUserId()).execute();
//            }
        } else if (v.getId() == R.id.refuse_video_call) {
            sendRejectEvent(VChatRejectParam.REASON_NORMAL);
            finish();
        }
    }
    private void sendRejectEvent(int reason){
//        VChatRejectParam param=new VChatRejectParam();
//        param.roomId=mParam.roomId;
//        param.gid=mParam.gid;
//        param.rejectId=ImUtils.getLoginUserId();
//        EventSender sender=EventSender.getInstance(this);
//        Map<String,String> map=new HashMap<>();
//        map.put("invite", JSON.toJSONString(param));
//        sender.sendEvent(EventType.V_CHAT_REJECT,mParam.fromUserId,map);
        VChatManager.sendRejectEvent(mParam, EventType.V_CHAT_REJECT, reason);
    }

    private void regReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                Log.d(TAG, "WL_DEBUG onReceive action = " + action);
                int result = intent.getIntExtra(VChatUtil.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);
                if (action.equals(VChatUtil.ACTION_ROOM_CREATE_COMPLETE)) {
                    if (result == AVError.AV_OK) {
//                    mQavsdkControl.getAVContext().getAudioCtrl().stopTRAEService();
//                    startActivityForResult(new Intent(mThis, VChatActivity.class)
//                            .putExtra(VChatUtil.EXTRA_RELATION_ID, mParam.roomId)
//                            .putExtra(VChatUtil.EXTRA_SELF_IDENTIFIER, ImSdk.getInstance().userId), 0);
//                    finish();
                    } else {
                        if ((mQavsdkControl != null) && (mQavsdkControl.getAVContext() != null) && (mQavsdkControl.getAVContext().getAudioCtrl() != null)) {
                            mQavsdkControl.getAVContext().getAudioCtrl().stopTRAEService();
                        }
                        ToastUtil.showToast(mThis, "进入房间:" + getString(R.string.error_code_prefix) + result);
                    }
                } else if (action.equals(VChatUtil.ACTION_CLOSE_ROOM_COMPLETE)) {
//				refreshWaitingDialog();
                } else if (action.equals(VChatUtil.ACTION_START_CONTEXT_COMPLETE)) {
                    if (result == AVError.AV_OK) {
//                    closeVoice();
                        mQavsdkControl.enterRoom(mParam.roomId, ImUtils.getLoginUserId());
                    } else {
                        ToastUtil.showToast(mThis, "启动视频:" + getString(R.string.error_code_prefix) + result);
                    }
                } else if (action.equals(VChatUtil.ACTION_MEMBER_CHANGE)) {
                    mQavsdkControl.enableSpeaker(false);
//                if(endpointNum==-1){
                    AVRoomMulti multi = (AVRoomMulti) mQavsdkControl.getRoom();
                    endpointNum = multi.getEndpointCount();
//                }
                    if (mDialog.isShowing()) {
                        goInChat();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(VChatUtil.ACTION_ROOM_CREATE_COMPLETE);
        filter.addAction(VChatUtil.ACTION_CLOSE_ROOM_COMPLETE);
        filter.addAction(VChatUtil.ACTION_START_CONTEXT_COMPLETE);
        filter.addAction(VChatUtil.ACTION_MEMBER_CHANGE);
        registerReceiver(mBroadcastReceiver, filter);
    }


    private void initMembers() {
        List<UserInfo> list = mParam.userList;
        if (list == null) {
            ChatGroupDao dao = new ChatGroupDao();
            ChatGroupPo po = dao.queryForId(mParam.gid);
            if (po != null) {
                List<UserInfo> groupUsers = JSON.parseArray(po.groupUsers, UserInfo.class);
                initMemberFromGroup(groupUsers);
            } else {
                initMemberFromGroup(null);
                fetchGroupInfo();
            }
        } else {
//            VChatManager.getInstance().addUserList(list);
            refreshMembersView(list);
        }
    }

    private void initMemberFromGroup(List<UserInfo> groupUsers) {
        List<UserInfo> list = new ArrayList<>();
        HashMap<String, UserInfo> uMap = new HashMap<>();
        if (groupUsers != null) {
            for (UserInfo temp : groupUsers) {
                uMap.put(temp.id, temp);
            }
        }
//        VChatManager man=VChatManager.getInstance();
        ArrayList<String> idList=new ArrayList<>();
        idList.addAll(mParam.idList);
        idList.add(mParam.fromUserId);
        for (String id :idList) {
            UserInfo u = uMap.get(id);
            if (u == null) {
                u = new UserInfo();
                u.id = id;
//                man.addUser(u);
            }
//            else{
//                man.updateUser(u);
//            }
//            if(id.equals(mParam.fromUserId)){
//                man.updateUser(u);
//            }else{
//                man.addInvite(u);
//            }
            list.add(u);
        }
        refreshMembersView(list);
    }
//    private void addOneMember(String id, HashMap<String, UserInfo> uMap,final List<UserInfo> list){
//        VChatManager man=VChatManager.getInstance();
//        UserInfo u = uMap.get(id);
//        if (u == null) {
//            u = new UserInfo();
//            u.id = id;
//            man.addUser(u);
//        }else{
//            man.updateUser(u);
//        }
//        list.add(u);
//    }

    private void fetchGroupInfo() {
        SessionGroup tool = new SessionGroup(this);
        tool.setCallbackNew(new SessionGroupCallbackNew() {
//            @Override
//            public void onGroupInfo(Data data, int what) {
//                List<UserInfo> list = Arrays.asList(data.userList);
//                initMemberFromGroup(list);
//            }

            @Override
            public void onGroupInfo(ChatGroupPo po, int what) {
                ChatGroupDao dao = new ChatGroupDao();
                po.updateStamp = 0;
                dao.saveGroup(po);
//                refreshMembersView(JSON.parseArray(po.groupUsers, UserInfo.class));
                initMemberFromGroup(JSON.parseArray(po.groupUsers, UserInfo.class));
            }

            @Override
            public void onGroupInfoFailed(String msg) {
                ToastUtil.showToast(mThis, "获取组信息失败");
            }
        });
        tool.getGroupInfoNew(mParam.gid);
//        tool.getGroupInfo(mParam.gid);
    }

    private void refreshMembersView(List<UserInfo> list) {
        mMemberLayout.removeAllViews();
        UserInfo me = null;
        VChatManager man=VChatManager.getInstance();
        for (UserInfo info : list) {
            if (ImUtils.getLoginUserId().equals(info.id)) {
                me = info;
                man.updateUser(info);
            } else if (!info.id.equals(mParam.fromUserId)) {
                man.addInvite(info);
                addMember(info.pic);
            } else {
                TextView tvName = (TextView) findViewById(R.id.name);
                tvName.setText(info.name);
                ImageView iv = (ImageView) findViewById(R.id.head_image);
                ImageLoader.getInstance().displayImage(info.pic, iv, ImUtils.getAvatarNormalImageOptions());
                man.updateUser(info);
            }
        }
        if (me != null)
            addMember(me.pic);
    }

    private void addMember(String picUrl) {
        ImageView v = new ImageView(this);
        int size = getResources().getDimensionPixelSize(R.dimen.vchat_invite_member_size);
        LayoutParams params = new LayoutParams(size, size);
        params.leftMargin = params.rightMargin = getResources().getDimensionPixelOffset(R.dimen.vchat_invite_member_margin);
        v.setLayoutParams(params);
        ImageLoader.getInstance().displayImage(picUrl, v, ImUtils.getAvatarRoundImageOptions());
        mMemberLayout.addView(v);
    }

    private void goInChat() {
        if (accepted) return;
        VChatManager.getInstance().startTime = System.currentTimeMillis();
        VChatManager.getInstance().curGroupId = mParam.gid;
        startActivity(new Intent(mThis, VChatActivity.class)
                .putExtra(VChatUtil.EXTRA_RELATION_ID, mParam.roomId)
                .putExtra(VChatUtil.EXTRA_SELF_IDENTIFIER, ImSdk.getInstance().userId));
        accepted = true;
        finish();
    }

    @Override
    public void finish() {
//        AVAudioCtrl ctrl=mQavsdkControl.getAVContext().getAudioCtrl();
//        ctrl.setAudioDataVolume(AVAudioCtrl.OUTPUT_MODE_SPEAKER, oldAudioVol);

        if (!accepted) {
            VChatManager.getInstance().clearRoom();
            if (mQavsdkControl.hasAVContext())
                mQavsdkControl.exitRoom();
            mQavsdkControl.stopContext();
        }
        super.finish();
    }

    public static void openUi(Context context, VChatInviteParam param) {
        Intent intent = new Intent(context, VChatInvitedActivity.class);
        intent.putExtra(VChatInvitedActivity.INTENT_PARAM, param);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private static class MyHandler extends Handler {
        WeakReference<VChatInvitedActivity> mActivityWeakReference;

        public MyHandler(VChatInvitedActivity activity) {
            this.mActivityWeakReference = new WeakReference<VChatInvitedActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VChatInvitedActivity activity = mActivityWeakReference.get();
            switch (msg.what) {
                case MSG_WHAT_TIMEOUT:
                    activity.sendRejectEvent(VChatRejectParam.REASON_TIMEOUT);
                    activity.finish();
                    break;
            }
        }
    }
}
