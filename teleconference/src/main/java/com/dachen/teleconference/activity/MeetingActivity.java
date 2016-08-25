package com.dachen.teleconference.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.utils.Logger;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.UIHelper;
import com.dachen.imsdk.entity.GroupInfo2Bean;
import com.dachen.teleconference.AgoraAPICallBack;
import com.dachen.teleconference.AgoraManager;
import com.dachen.teleconference.R;
import com.dachen.teleconference.adapter.UserAdapter;
import com.dachen.teleconference.bean.GetMediaDynamicKeyResponse;
import com.dachen.teleconference.bean.GetSigningKeyResponse;
import com.dachen.teleconference.common.BaseActivity;
import com.dachen.teleconference.http.HttpCommClient;
import com.dachen.teleconference.views.CallMeetingMemberDialog;
import com.dachen.teleconference.views.FloatingView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

/**
 * 会议界面
 *
 * @author gzhuo
 * @date 2016/8/17
 */
public class MeetingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MeetingActivity.class.getSimpleName();
    private static final String INTENT_EXTRA_GROUP_USER_LIST = "user_list";
    private static final String INTENT_EXTRA_USER_ID = "user_id";
    private static final String INTENT_EXTRA_GROUP_ID = "group_id";
    private static final int GET_SIGNNING_KEY = 1001;
    private static final int GET_MEDIADYNAMIC_KEY = 1002;
    private String mVendorKey;
    private String mDynamicKey;
    private String mChannelId;//房间号
    private AgoraManager mAgoraManager;
    private RtcEngine mRtcEngine;
    private RtcEngineEventHandler mRtcEngineEventHandler;
    private RecyclerView mRecyclerView;
    private TextView mLeftBtn;
    private TextView mTitle;
    private TextView mRightBtn;
    private ImageView mSpeakerIv;
    private ImageView mHangIv;
    private ImageView mMutIv;
    private boolean isSpeakerOn = true;
    private boolean isMutOn = false;
    private UserAdapter mAdapter;
    private List<GroupInfo2Bean.Data.UserInfo> mUserInfos = new ArrayList<>();
    private String mUserId;

    private static final String vendorKey = "86c6c121ff444021a5152b0a791aefd3";
    public static final String signKey = "b9aec320141347c6b64226cb7e901d23";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_SIGNNING_KEY:
                    if (msg.arg1 == 1) {
                        if (msg.obj != null) {
                            GetSigningKeyResponse res = (GetSigningKeyResponse) msg.obj;
                            String data = res.getData();
                            AgoraManager.getInstance(MeetingActivity.this).loginAgora(mUserId, data, vendorKey);
                        }
                    } else {
                        UIHelper.ToastMessage(MeetingActivity.this, (String) msg.obj);
                    }

                    break;

                case GET_MEDIADYNAMIC_KEY:
                    if (msg.arg1 == 1) {
                        if (msg.obj != null) {
                            AgoraManager.getInstance(MeetingActivity.this).joinChannel("1001", ((GetMediaDynamicKeyResponse) msg
                                    .obj).getData(), Integer.parseInt(mUserId));
                        }
                    } else {
                        UIHelper.ToastMessage(MeetingActivity.this, (String) msg.obj);
                    }
                    break;
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        initVariables();

        initView();

        initAgoraConfigure();

        loginAndjoinChannel();
    }

    private void initView() {
        mLeftBtn = (TextView) findViewById(R.id.left_btn);
        mTitle = (TextView) findViewById(R.id.title);
        mRightBtn = (TextView) findViewById(R.id.right_btn);
        mSpeakerIv = (ImageView) findViewById(R.id.speaker_iv);
        mHangIv = (ImageView) findViewById(R.id.hang_iv);
        mMutIv = (ImageView) findViewById(R.id.mut_iv);
        mRecyclerView = (RecyclerView) findViewById(R.id.room_view);


        mLeftBtn.setText("隐藏");
        mTitle.setText("电话会议");
        mRightBtn.setText("全部静音");

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        mSpeakerIv.setOnClickListener(this);
        mHangIv.setOnClickListener(this);
        mMutIv.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(MeetingActivity.this, 4));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new GridDividerItemDecoration(2, 3));

        mAdapter = new UserAdapter(MeetingActivity.this, mUserInfos, mUserId);
        mAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (position == 0) {
                    ToastUtil.showToast(MeetingActivity.this, "添加成员");
                    return;
                }
                GroupInfo2Bean.Data.UserInfo userInfo = mUserInfos.get(position - 1);
                CallMeetingMemberDialog callMeetingMemberDialog = new CallMeetingMemberDialog(MeetingActivity.this, userInfo.name,
                        userInfo.pic);
                callMeetingMemberDialog.show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    private void hide() {
        FloatingView floatingView = new FloatingView(this);
        floatingView.show();

    }

    /**
     * 登录并加入房间
     */
    private void loginAndjoinChannel() {
        //        mRtcEngine.joinChannel(mDynamicKey, mChannelId, "", Integer.parseInt(mUserId));
        HttpCommClient.getInstance().getSigningKey(this, mHandler, GET_SIGNNING_KEY, mUserId, "3600");
    }

    private void initVariables() {
        Intent intent = getIntent();
        mVendorKey = intent.getStringExtra("vendor_key");
        mDynamicKey = intent.getStringExtra("dynamic_key");
        mChannelId = intent.getStringExtra("channel_id");
        List<GroupInfo2Bean.Data.UserInfo> userInfos = (List<GroupInfo2Bean.Data.UserInfo>) intent.getSerializableExtra(
                INTENT_EXTRA_GROUP_USER_LIST);

        mUserId = intent.getStringExtra(INTENT_EXTRA_USER_ID);
        for (GroupInfo2Bean.Data.UserInfo info : userInfos) {
            if (info.id.equals(mUserId)) {
                mUserInfos.clear();
                mUserInfos.add(info);
            }
        }
        for (GroupInfo2Bean.Data.UserInfo info : userInfos) {
            if (!info.id.equals(mUserId)) {
                mUserInfos.add(info);
            }
        }
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
            ImageLoader.getInstance().init(config);
        }
    }

    private void initAgoraConfigure() {

    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.left_btn) {
            hide();
        } else if (i == R.id.right_btn) {
            ToastUtil.showToast(MeetingActivity.this, "全部静音");
        } else if (i == R.id.speaker_iv) {
            setSpeaker();
        } else if (i == R.id.hang_iv) {
            finish();
        } else if (i == R.id.mut_iv) {
            setMut();
        }

    }

    private void setSpeaker() {
        if (isSpeakerOn) {
            isSpeakerOn = false;
            mSpeakerIv.setImageResource(R.drawable.speaker_close);
        } else {
            isSpeakerOn = true;
            mSpeakerIv.setImageResource(R.drawable.speaker_on);
        }
    }

    private void setMut() {
        if (isMutOn) {
            isMutOn = false;
            mMutIv.setImageResource(R.drawable.mut_close);
        } else {
            isMutOn = true;
            mMutIv.setImageResource(R.drawable.mut_on);
        }

    }

    /**
     * agora事件回调
     */
    class RtcEngineEventHandler extends IRtcEngineEventHandler {

        @Override
        public void onUserJoined(int uid, int elapsed) {
            Logger.d(TAG, "onUserJoined uid = " + uid);
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Logger.d(TAG, "onJoinChannelSuccess uid = " + uid);
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            Logger.d(TAG, "onLeaveChannel");
        }
    }


    public static void openUI(Context context, String userId, String groupId, ArrayList<GroupInfo2Bean.Data.UserInfo>
            userList) {
        Intent intent = new Intent(context, MeetingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_EXTRA_USER_ID, userId);
        intent.putExtra(INTENT_EXTRA_GROUP_ID, groupId);
        intent.putExtra(INTENT_EXTRA_GROUP_USER_LIST, userList);
        context.startActivity(intent);
    }


}
