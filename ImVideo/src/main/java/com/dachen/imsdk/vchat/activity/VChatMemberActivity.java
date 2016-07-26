package com.dachen.imsdk.vchat.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.dachen.common.utils.ToastUtil;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.vchat.R;;
import com.dachen.imsdk.activities.ChatActivityV2;
import com.dachen.imsdk.activities.ImBaseActivity;
import com.dachen.imsdk.consts.EventType;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.entity.VChatInviteParam;
import com.dachen.imsdk.net.EventSender;
import com.dachen.imsdk.net.EventSender.OnResultListener;
import com.dachen.imsdk.net.PushSender;
import com.dachen.imsdk.utils.ImUtils;
import com.dachen.imsdk.vchat.ImVideo;
import com.dachen.imsdk.vchat.MemberInfo;
import com.dachen.imsdk.vchat.VChatManager;
import com.dachen.imsdk.vchat.VChatUtil;
import com.dachen.imsdk.vchat.adapter.VChatMemberAdapter;
import com.dachen.imsdk.vchat.control.QavsdkControl;
import com.dachen.imsdk.vchat.work.InitVChatTasks;
import com.tencent.av.sdk.AVError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Mcp on 2016/3/25.
 */
public class VChatMemberActivity extends ImBaseActivity implements OnClickListener {
    public static final String TAG = "VChatMemberActivity";
    public static final String INTENT_MEMBER_LIST = "memberList";
    public static final String INTENT_OLD_LIST = "oldList";
    public static final String INTENT_ROOM_ID = "roomId";

    private ArrayList<UserInfo> memberList;
    private ArrayList<String> oldList;
    private VChatMemberAdapter mAdapter;
    private String mGroupId;
    private boolean isCreate;
    private boolean hasEnterRoom;
    private boolean eventOk;
    private int roomId;
    private UserInfo myInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vchat_menber_activity);
//        memberList = (ArrayList<UserInfo>) getIntent().getSerializableExtra(INTENT_MEMBER_LIST);
//        oldList= (ArrayList<UserInfo>) getIntent().getSerializableExtra(INTENT_OLD_LIST);
        initOldList();
        roomId = getIntent().getIntExtra(INTENT_ROOM_ID, 0);
        mGroupId = getIntent().getStringExtra(ChatActivityV2.INTENT_EXTRA_GROUP_ID);
        if (roomId == 0) {
            roomId = VChatUtil.makeRoomId();
            isCreate = true;
        }
        if (memberList == null) {
            ChatGroupDao dao = new ChatGroupDao();
            ChatGroupPo po = dao.queryForId(mGroupId);
            if (po == null) {
                ToastUtil.showToast(this, "获取用户列表失败");
                finish();
                return;
            }
            List<UserInfo> list = JSON.parseArray(po.groupUsers, UserInfo.class);
            memberList = new ArrayList<>(list);
        }
        mAdapter = new VChatMemberAdapter(this, memberList, oldList);
        ListView lv = (ListView) findViewById(R.id.list_view);
        lv.setAdapter(mAdapter);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
        if (isCreate) {
            regReceiver();
        }
    }

    private void initOldList() {
        oldList = new ArrayList<>();
        List<MemberInfo> mList = ImVideo.getInstance().mQavsdkControl.getMemberList();
        for (MemberInfo info : mList) {
            oldList.add(info.identifier);
        }
        VChatManager man=VChatManager.getInstance();
        for (UserInfo info : man.allUserList) {
            oldList.add(info.id);
        }
    }

    @Override
    protected void onDestroy() {
        if (isCreate)
            unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            doInvite();
        } else if (v.getId() == R.id.back_btn) {
            finish();
        }
    }


    private void doInvite() {
        if (isCreate && VChatManager.getInstance().isInChat) {
            return;
        }
        if(mAdapter.getInviteList().size()==0){
            ToastUtil.showToast(mThis,"请选择视频成员");
            return;
        }
        for (UserInfo user : memberList) {
            if (ImUtils.getLoginUserId().equals(user.id)) {
                myInfo = user;
            }
        }
        if (myInfo == null) {
            ToastUtil.showToast(mThis, "你已经不属于此会话组");
            return;
        }
        final QavsdkControl control = ImVideo.getInstance().mQavsdkControl;
        if (!isCreate) {
            sendEvent();
        } else if (control.hasAVContext()) {
            if (!control.getIsInEnterRoom())
                control.enterRoom(roomId, ImUtils.getLoginUserId());
//            mDialog.setMessage("正在进入房间");
            mDialog.show();
        } else {
//            ToastUtil.showToast(this, "腾讯SDK未启动,重新启动腾讯SDK");
            new InitVChatTasks(ImUtils.getLoginUserId()).execute();
//            mDialog.setMessage("正在启动视频");
            mDialog.show();
        }
    }

    private void sendEvent() {
        String toId = "";
        String toAddId = "";
        VChatInviteParam param = new VChatInviteParam();
        param.fromUserId = ImUtils.getLoginUserId();
//        UserInfo myInfo = null;
//        for (UserInfo user : memberList) {
//            if (ImUtils.getLoginUserId().equals(user.id)) {
//                myInfo = user;
//            }
//        }
//        if (myInfo == null) {
//            ToastUtil.showToast(mThis, "你已经不属于此会话组");
//            return;
//        }
        final HashSet<UserInfo> inviteList = mAdapter.getInviteList();
        List<String> toUserArr = new ArrayList<>();
        param.gid = mGroupId;
        param.roomId = roomId;
        VChatManager man = VChatManager.getInstance();
        final ArrayList<UserInfo> list = new ArrayList<>();
        for (UserInfo u : man.allUserList) {
            if (ImUtils.getLoginUserId().equals(u.id)) continue;
            toAddId += u.id + "|";
            list.add(u);
        }
        for (UserInfo u : inviteList) {
            toId += u.id + "|";
            toUserArr.add(u.id);
            if (man.getUserIndex(u.id) < 0) {
                list.add(u);
            }
        }
//        list.addAll(inviteList);
        list.add(myInfo);
        param.userList = list;
//            param.userList.addAll(mAdapter.getInviteList());
        final EventSender sender = EventSender.getInstance(this);
        final Map<String, String> map = new HashMap<>();
        map.put("invite", JSON.toJSONString(param));
        final String finalToAddId = toAddId;
        sender.sendEvent(EventType.V_CHAT_INVITE, toId, map, new OnResultListener() {
            @Override
            public void onResult(boolean isSuccess) {
                if (isSuccess) {
                    VChatManager.getInstance().addInviteList(inviteList);
                    if (isCreate) {
                        eventOk = true;
                        VChatManager.getInstance().startTime = System.currentTimeMillis();
                        startActivityForResult(new Intent(mThis, VChatActivity.class)
                                .putExtra(VChatUtil.EXTRA_RELATION_ID, roomId)
                                .putExtra(VChatUtil.EXTRA_SELF_IDENTIFIER, ImSdk.getInstance().userId), 0);
                        finish();
                    } else {
                        sender.sendEvent(EventType.V_CHAT_ADD_USER, finalToAddId, map);
                        ToastUtil.showToast(mThis, "已发送邀请");
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    ToastUtil.showToast(mThis, "发送指令失败");
                }
            }
        });
        //发送推送
        Map<String, String> params2 = new HashMap<String, String>();
        toUserArr.add(ImUtils.getLoginUserId());
        param.idList = toUserArr;
        param.userList = null;
        params2.put("invite", JSON.toJSONString(param));
        params2.put("push_sound", "videoNotice.mp3");
//        params2.put("invite", "test");
        PushSender.getInstance(this).sendPushMessage(ImSdk.getInstance().userName+"医生邀请您加入视频通话", toUserArr.toArray(new String[toUserArr.size()]), params2, true);
    }

    private void regReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(VChatUtil.ACTION_ROOM_CREATE_COMPLETE);
        filter.addAction(VChatUtil.ACTION_CLOSE_ROOM_COMPLETE);
        filter.addAction(VChatUtil.ACTION_START_CONTEXT_COMPLETE);
        registerReceiver(mBroadcastReceiver, filter);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "WL_DEBUG onReceive action = " + action);
            int result = intent.getIntExtra(VChatUtil.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);
            QavsdkControl mQavsdkControl = ImVideo.getInstance().mQavsdkControl;
            if (action.equals(VChatUtil.ACTION_ROOM_CREATE_COMPLETE)) {
//				handler.removeMessages(MSG_CREATEROOM_TIMEOUT);
//				refreshWaitingDialog();
                VChatManager.getInstance().callerId = ImUtils.getLoginUserId();
                int mCreateRoomErrorCode = intent.getIntExtra(VChatUtil.EXTRA_AV_ERROR_RESULT, AVError.AV_OK);

                if (mCreateRoomErrorCode == AVError.AV_OK) {
                    hasEnterRoom = true;
                    VChatManager.getInstance().curGroupId = mGroupId;
//                    startActivityForResult(new Intent(mThis, VChatActivity.class)
//                            .putExtra(VChatUtil.EXTRA_RELATION_ID, roomId)
//                            .putExtra(VChatUtil.EXTRA_SELF_IDENTIFIER, ImSdk.getInstance().userId), 0);
//                    finish();
                    sendEvent();
//                    mDialog.setMessage("正在发送指令");
                    mDialog.show();
                } else {
                    if ((mQavsdkControl != null) && (mQavsdkControl.getAVContext() != null) && (mQavsdkControl.getAVContext().getAudioCtrl() != null)) {
                        mQavsdkControl.getAVContext().getAudioCtrl().stopTRAEService();
                    }
                    mDialog.dismiss();
                    ToastUtil.showToast(mThis, "进入房间:" + getString(R.string.error_code_prefix) + mCreateRoomErrorCode);
                }
            } else if (action.equals(VChatUtil.ACTION_CLOSE_ROOM_COMPLETE)) {
//				refreshWaitingDialog();
            } else if (action.equals(VChatUtil.ACTION_START_CONTEXT_COMPLETE)) {
                if (result == AVError.AV_OK) {
//                    closeVoice();
                    mQavsdkControl.enterRoom(roomId, ImUtils.getLoginUserId());
//                    mDialog.setMessage("正在进入房间");
                } else {
                    ToastUtil.showToast(mThis, "启动视频:" + getString(R.string.error_code_prefix) + result);
                }
            }
        }
    };

    @Override
    public void finish() {
        QavsdkControl mQavsdkControl = ImVideo.getInstance().mQavsdkControl;
        if (hasEnterRoom && !eventOk) {
            mQavsdkControl.exitRoom();
            VChatManager.getInstance().clearRoom();
        }
        if (isCreate && !eventOk)
            mQavsdkControl.stopContext();
        super.finish();
    }
}
