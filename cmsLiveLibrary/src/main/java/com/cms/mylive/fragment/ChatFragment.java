package com.cms.mylive.fragment;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.cms.mylive.MyLiveActivity;
import com.cms.mylive.R;
import com.cms.mylive.adapter.GridViewAvatarAdapter;
import com.cms.mylive.adapter.GridViewAvatarAdapter.SelectAvatarInterface;
import com.cms.mylive.adapter.OnChatAdapter;
import com.cms.mylive.callbacks.MyIChatCallBack;
import com.cms.mylive.chat.PrivateChatManager;
import com.cms.mylive.chat.PrivateChatMessage;
import com.cms.mylive.chat.PublicChatManager;
import com.cms.mylive.chat.PublicChatMessage;
import com.gensee.room.RTRoom;
import com.gensee.room.RtSdk;
import com.gensee.routine.UserInfo;
import com.gensee.taskret.OnTaskRet;
import com.gensee.view.ChatEditText;

public class ChatFragment extends Fragment {

	// @ViewInject(R.id.frient_sppiner)
	private Spinner mSpinner;
	private ArrayAdapter<String> adapter;
	private ImageView btnSend;
	private MyLiveActivity mMainActivity;
	private RtSdk mRtSdk;
	private List<UserInfo> mAllUser;
	private String[] users;
	private long mUserID = -1000;
	private OnChatAdapter mOnChatAdapter;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mOnChatAdapter.init(mUserID);
			mListView.setSelection(mOnChatAdapter.getCount()-1);

		};
	};
	private View view;
	private BroadcastReceiver receiver;
	private boolean isFirst = true;
	private ChatEditText mChatEditText;
	private ListView mListView;
	private GridView mGridView;
	private GridViewAvatarAdapter mGridViewAvatarAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mMainActivity = (MyLiveActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.chat_fragment, null);

		initView(view);
		initData();
		return view;
	}


	private void initView(View view) {
		// 设置会议的所有用户数
		mSpinner = (Spinner) view.findViewById(R.id.frient_sppiner);
		btnSend = (ImageView) view.findViewById(R.id.btnSend);
		mChatEditText = (ChatEditText) view.findViewById(R.id.chat_edittext);
		mListView = (ListView) view.findViewById(R.id.lvChat);
		btnSend.setOnClickListener(new MyOnClickListener());
		mOnChatAdapter = new OnChatAdapter(mMainActivity);
		mListView.setAdapter(mOnChatAdapter);


	}
	private void initData() {
		receiver = new MyBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.cms.FINISH_USERS");
		mMainActivity.registerReceiver(receiver, filter);
	}

	private void initSpinnerData() {
		mRtSdk = mMainActivity.getRtSdk();
		mRtSdk.setChatCallback(new IChatCallBackImpl());
//		LogUtils.getInstance().toast(mMainActivity, mAllUser.size()+"人");
		mAllUser = mRtSdk.getAllUsers();
		users = new String[mAllUser.size() + 1];
		users[0] = "所有人";

		for (int i = 0; i < mAllUser.size(); i++) {
			users[i + 1] = mAllUser.get(i).getName();
		}
		adapter = new ArrayAdapter<>(mMainActivity,R.layout.spinner_item,R.id.tv, users);
		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}

	//富文本
	public class MySelectAvatarInterface implements SelectAvatarInterface{

		@Override
		public void selectAvatar(String sAvatar, Drawable resId) {
			mChatEditText.insertAvatar(sAvatar,0);

		}

	}

	// 广播接受者
	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			initSpinnerData();
		}

	}

	// spinner选择事件
	private class MyOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
								   int position, long id) {
			if (position == 0) {
				mUserID = -1000;
			} else {
				mUserID = mAllUser.get(position - 1).getId();
			}
			mHandler.sendEmptyMessage(0);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	// 信息发送
	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btnSend) {
				String mStringText = mChatEditText.getText().toString();
				if (!TextUtils.isEmpty(mStringText)) {
					String chatText = mChatEditText.getChatText();
					String richText = mChatEditText.getRichText();
					if (mUserID == -1000) {
						mRtSdk.chatWithPublic(mStringText, richText,
								new MyOnTaskRet());
					} else {
						mRtSdk.chatWithPersion(mStringText, richText, mUserID,
								new MyOnTaskRet());
					}
					// 清空内容
					mChatEditText.setText("");
				}
			}
//			else if (v.getId() == R.id.chat_expression) {
//
//				if (mGridView.getVisibility() == View.GONE) {
//					mGridView.setVisibility(View.VISIBLE);
//				} else if (mGridView.getVisibility() == View.VISIBLE) {
//					mGridView.setVisibility(View.GONE);
//				}
//				if (mGridViewAvatarAdapter == null) {
//					mGridViewAvatarAdapter = new GridViewAvatarAdapter(
//							mGridView.getContext(), new MySelectAvatarInterface());
//					mGridView.setAdapter(mGridViewAvatarAdapter);
//				} else {
//					mGridViewAvatarAdapter.notifyDataSetChanged();
//				}
//
//			}
		}
	}

	private class MyOnTaskRet implements OnTaskRet {

		@Override
		public void onTaskRet(boolean arg0, int arg1, String arg2) {
			mHandler.sendEmptyMessage(0);
		}

	}

	// 聊天对调接口
	private class IChatCallBackImpl extends MyIChatCallBack {
		@Override
		public void onChatJoinConfirm(boolean result) {
			// if(result)
			// initSpinnerData();
		}

		@Override
		public void onChatWithPersion(UserInfo userInfo, String msg, String rich) {

			PrivateChatMessage message = new PrivateChatMessage();
			message.setText(msg);
			message.setTime(Calendar.getInstance().getTimeInMillis());
			message.setSendUserId(userInfo.getId());
			message.setRich(rich);
			message.setSendUserName(userInfo.getName());
			PrivateChatManager.getIns().addMsg(userInfo.getId(), message);
			if (mUserID == userInfo.getId()) {
				mHandler.sendEmptyMessage(0);
			}
		}

		@Override
		public void onChatWithPublic(UserInfo userInfo, String msg, String rich) {

			PublicChatMessage mPublicChatMessage = new PublicChatMessage();
			mPublicChatMessage.setText(msg);
			mPublicChatMessage.setRich(rich);
			mPublicChatMessage.setSendUserName(userInfo.getName());
			mPublicChatMessage
					.setTime(Calendar.getInstance().getTimeInMillis());
			PublicChatManager.getIns().addMsg(mPublicChatMessage);
			if (mUserID == -1000) {
				mHandler.sendEmptyMessage(0);
			}
		}

		@Override
		public void onChatToPersion(long userId, String msg, String rich) {
			PrivateChatMessage message = new PrivateChatMessage();
			message.setText(msg);
			message.setTime(Calendar.getInstance().getTimeInMillis());
			message.setSendUserId(RTRoom.getIns().getUserId());
			message.setRich(rich);
			message.setReceiveUserId(userId);
			message.setSendUserName(mRtSdk.getSelfUserInfo().getName());
			PrivateChatManager.getIns().addMsg(userId, message);
			mHandler.sendEmptyMessage(0);
		}

		@Override
		public void onChatEnable(boolean enable) {
		}

	}

	// 解除广播接受者
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null)
			mMainActivity.unregisterReceiver(receiver);
	}

}
