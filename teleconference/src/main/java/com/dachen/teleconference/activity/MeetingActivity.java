package com.dachen.teleconference.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.utils.Logger;
import com.dachen.common.utils.ToastUtil;
import com.dachen.teleconference.AgoraManager;
import com.dachen.teleconference.R;
import com.dachen.teleconference.adapter.UserAdapter;
import com.dachen.teleconference.bean.User;
<<<<<<< HEAD
import com.dachen.teleconference.common.BaseActivity;
=======
import com.dachen.teleconference.views.CallMeetingMemberDialog;
>>>>>>> c0d0d55852673bb3c305fddb3b1c455597b3cd65
import com.dachen.teleconference.views.FloatingView;
import com.dachen.teleconference.views.RoomView;
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
<<<<<<< HEAD
public class MeetingActivity extends BaseActivity {
=======
public class MeetingActivity extends Activity implements View.OnClickListener {
>>>>>>> c0d0d55852673bb3c305fddb3b1c455597b3cd65
    private static final String TAG = MeetingActivity.class.getSimpleName();
    private String mVendorKey;
    private String mDynamicKey;
    private String mChannelId;//房间号
    private String mUserId;
    private AgoraManager mAgoraManager;
    private RtcEngine mRtcEngine;
    private RtcEngineEventHandler mRtcEngineEventHandler;
    private RoomView mRoomView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        initVariables();

        initView();

        initRtcEngine();

        joinChannel();
    }

    private void initView() {
        mLeftBtn = (TextView) findViewById(R.id.left_btn);
        mTitle = (TextView) findViewById(R.id.title);
        mRightBtn = (TextView) findViewById(R.id.right_btn);
        mSpeakerIv = (ImageView) findViewById(R.id.speaker_iv);
        mHangIv = (ImageView) findViewById(R.id.hang_iv);
        mMutIv = (ImageView) findViewById(R.id.mut_iv);
        //        mRoomView = (RoomView) findViewById(R.id.roomView);
        mRecyclerView = (RecyclerView) findViewById(R.id.room_view);


        mLeftBtn.setText("隐藏");
        mTitle.setText("电话会议");
        mRightBtn.setText("全部静音");

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        mSpeakerIv.setOnClickListener(this);
        mHangIv.setOnClickListener(this);
        mMutIv.setOnClickListener(this);

        List<List<User>> data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<User> userList = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                User user = new User();
                user.id = (i + 1) * (j + 1) + "";
                user.name = "王宝强 " + (i + 1) * (j + 1);
                user.head = "http://h.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=1904b984fdfaaf5190ee89eded3dff8b/aec379310a55b3193cdb93d743a98226cffc1775.jpg";
                userList.add(user);
            }
            data.add(userList);
        }

        //        mRoomView.setData(data);

        List<User> userList = new ArrayList<>();
        for (int j = 0; j < 15; j++) {
            User user = new User();
            user.id = (j + 1) + "";
            user.name = "王宝强 " + (j + 1);
            user.head = "http://h.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=1904b984fdfaaf5190ee89eded3dff8b/aec379310a55b3193cdb93d743a98226cffc1775.jpg";
            userList.add(user);
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(MeetingActivity.this, 4));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new GridDividerItemDecoration(2, 3));
        mAdapter = new UserAdapter(MeetingActivity.this, userList);
        mAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (position == 0) {
                    ToastUtil.showToast(MeetingActivity.this, "添加成员");
                    return;
                }
                CallMeetingMemberDialog callMeetingMemberDialog = new CallMeetingMemberDialog(MeetingActivity.this);
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
     * 加入房间
     */
    private void joinChannel() {
        mRtcEngine.joinChannel(mDynamicKey, mChannelId, "", Integer.parseInt(mUserId));
    }

    private void initVariables() {
        Intent intent = getIntent();
        mVendorKey = intent.getStringExtra("vendor_key");
        mDynamicKey = intent.getStringExtra("dynamic_key");
        mChannelId = intent.getStringExtra("channel_id");
        mUserId = intent.getStringExtra("user_id");

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
            ImageLoader.getInstance().init(config);
        }
    }

    private void initRtcEngine() {
        /*mAgoraManager = AgoraManager.getInstance(this);
        mAgoraManager.createRtcEngine(mVendorKey);
        mRtcEngine = mAgoraManager.getRtcEngine();
        mRtcEngineEventHandler = new RtcEngineEventHandler();
        mAgoraManager.getEventHandlerMgr().addRtcEngineEventHandler(mRtcEngineEventHandler);*/
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
}
