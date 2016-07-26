package com.dachen.imsdk.vchat;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.dachen.common.utils.ToastUtil;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.entity.VChatInviteParam;
import com.dachen.imsdk.entity.VChatRejectParam;
import com.dachen.imsdk.net.EventSender;
import com.dachen.imsdk.utils.ImUtils;
import com.dachen.imsdk.vchat.activity.VChatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.greenrobot1.event.EventBus;

/**
 * Created by Mcp on 2016/3/22.
 */
public class VChatManager {
    private static VChatManager instance;
    public int curRoomId;
    public String callerId;
    public String curGroupId;
    public boolean isInInvite;
    public boolean isInChat;
    //    public Map<String ,UserInfo> allUserMap =new HashMap<>();
    public ArrayList<UserInfo> myInviteList = new ArrayList<>();
    public ArrayList<UserInfo> allUserList = new ArrayList<>();
    public long startTime;
    private Handler mHandler;
    private static final int MSG_CHECK_INVITE=101;

    private VChatManager(){
        mHandler=new Handler(ImSdk.getInstance().context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_CHECK_INVITE:
                        String id= (String) msg.obj;
                        removeInvite(id);
                        break;
                }
            }
        };
    }

    public synchronized static VChatManager getInstance() {
        if (instance == null) {
            instance = new VChatManager();
        }
        return instance;
    }

    public void clearRoom() {
        curRoomId = 0;
        isInChat = false;
//        allUserMap.clear();
        allUserList.clear();
        myInviteList.clear();
        startTime = 0;
        mHandler.removeMessages(MSG_CHECK_INVITE);
    }

    public void addUsers(Map<String, UserInfo> map, List<UserInfo> users) {
        for (UserInfo u : users) {
            map.put(u.id, u);
        }
    }

    public void addInvite(UserInfo u){
        int invIndex=getInviteIndex(u.id);
        if (invIndex < 0) {
            myInviteList.add(u);
            Message msg=mHandler.obtainMessage(MSG_CHECK_INVITE);
            msg.obj=u.id;
            mHandler.sendMessageDelayed(msg,60*1000);
//                allUserMap.put(u.id,u);
        }else{
            myInviteList.set(invIndex,u);
        }
        updateUser(u);
    }

    public void addInviteList(HashSet<UserInfo> users) {
        for (UserInfo u : users) {
            if (getInviteIndex(u.id) < 0) {
                myInviteList.add(u);
                Message msg=mHandler.obtainMessage(MSG_CHECK_INVITE);
                msg.obj=u.id;
                mHandler.sendMessageDelayed(msg,60*1000);
//                allUserMap.put(u.id,u);
            }
            if (getUserIndex(u.id) < 0) {
                allUserList.add(u);
            }
        }
        EventBus.getDefault().post(new VChatUserChangeEvent());
    }

    public void addUserList(Collection<UserInfo> users) {
        if (users == null) return;
        for (UserInfo u : users) {
            if (getUserIndex(u.id) < 0) {
                allUserList.add(u);
            }
        }
        EventBus.getDefault().post(new VChatUserChangeEvent());
    }

    public void addUser(UserInfo u) {
        if (getUserIndex(u.id) < 0) {
            allUserList.add(u);
        }
    }

    public int getInviteIndex(String id) {
        for (int i = 0; i < myInviteList.size(); i++) {
            UserInfo u = myInviteList.get(i);
            if (u.id.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public int getUserIndex(String id) {
        for (int i = 0; i < allUserList.size(); i++) {
            UserInfo u = allUserList.get(i);
            if (u.id.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public void updateUser(UserInfo u) {
        int i = getUserIndex(u.id);
        if (i < 0) {
            allUserList.add(u);
        } else {
            allUserList.set(i, u);
        }
    }

    public UserInfo removeInvite(String userId) {
        return removeInvite(userId,true);
    }
    public UserInfo removeInvite(String userId,boolean removeUser) {
        UserInfo u = null;
        int i = getInviteIndex(userId);
        VChatActivity act = VChatActivity.getInstance();
        if (i >= 0) {
            u = myInviteList.get(i);
            myInviteList.remove(i);
            if(removeUser)
                removeUser(userId);

            if (act != null) {
//            act.showMemberIn(u.name);
                act.refreshInviteList();
            }
        }
        return u;
    }

    public UserInfo removeUser(String userId) {
        return removeUser(userId,false);
    }
    public UserInfo removeUser(String userId,boolean toastExit) {
        UserInfo u = null;
        int n = getUserIndex(userId);
        if (n >= 0) {
            u=allUserList.get(n);
            if(toastExit){
                ToastUtil.showToast(ImSdk.getInstance().context,u.name+"已退出视频");
            }
            allUserList.remove(n);
            EventBus.getDefault().post(new VChatUserRemoveEvent(userId));
        }

        return u;
    }

    public static class VChatLayoutEvent {
    }
    public static class VChatUserEmptyEvent {
    }

    public static class VChatUserChangeEvent {
    }

    public static class VChatUserRemoveEvent {
        public String userId;

        public VChatUserRemoveEvent(String id) {
            this.userId = id;
        }
    }

    public static void sendRejectEvent(VChatInviteParam mParam, int type, int reason) {
        VChatRejectParam param = new VChatRejectParam();
        param.roomId = mParam.roomId;
        param.gid = mParam.gid;
        param.rejectId = ImUtils.getLoginUserId();
        param.reason = reason;
        EventSender sender = EventSender.getInstance(ImSdk.getInstance().context);
        Map<String, String> map = new HashMap<>();
        map.put("invite", JSON.toJSONString(param));
        String toId = "";
        if (mParam.userList != null) {
            for (UserInfo u : mParam.userList) {
                if (ImUtils.getLoginUserId().equals(u.id)) continue;
                toId += u.id + "|";
            }
        } else if (mParam.idList != null) {
            for (String id : mParam.idList) {
                if (ImUtils.getLoginUserId().equals(id)) continue;
                toId += id + "|";
            }
        }
        sender.sendEvent(type, toId, map);
    }
}
