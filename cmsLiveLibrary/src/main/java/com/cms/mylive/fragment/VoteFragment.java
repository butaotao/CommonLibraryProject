package com.cms.mylive.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.mylive.MyLiveActivity;
import com.cms.mylive.R;
import com.cms.mylive.adapter.VoteGroupListAdapter;
import com.cms.mylive.callbacks.MyIVoteCallBack;
import com.cms.mylive.utils.LogUtils;
import com.cms.mylive.view.MyPopupWindow;
import com.gensee.room.RtSdk;
import com.gensee.vote.VoteGroup;

public class VoteFragment extends Fragment {
	public static final int VOTE_ADD = 0;
	public static final int VOTE_DEL = VOTE_ADD + 2;
	public static final int VOTE_PUBLISH = VOTE_ADD + 3;
	public static final int VOTE_PUBLISH_RESULT = VOTE_ADD + 4;
	public static final int VOTE_DEADLINE = VOTE_ADD + 5;
	public static final int VOTE_SUBMI = VOTE_ADD + 6;
	private MyLiveActivity mMainActivity;
	private RtSdk mRtSdk;
	private LinearLayout ll;
	private RelativeLayout rl;

	List<VoteGroup> mVoteGroupList = new ArrayList<VoteGroup>();
	List<String> mVoteThemeList = new ArrayList<>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String text = "";
			switch (msg.what) {
				case VOTE_ADD:
					text = "voteAdd";
					break;
				case VOTE_DEADLINE:
					text = "voteDeadline";

					break;
				case VOTE_DEL:
					text = "voetDel";
					break;
				case VOTE_SUBMI:
					break;
				case VOTE_PUBLISH:
					text = "votePublish";
					// 这个地方，更新收到的数据
					VoteGroup mVoteGroup = (VoteGroup) msg.obj;
					checkVoteGroupList(mVoteGroup);
					// 显示投票的popupwindow
					new MyPopupWindow(mMainActivity, mRtSdk).show(mVoteGroup,
							mVoteGroupList);
					break;
				case VOTE_PUBLISH_RESULT:
					text="vote_publish_resutl";
					if(!ll.isShown()){
						ll.setVisibility(View.VISIBLE);
						rl.setVisibility(View.GONE);
					}
					// 显示投票的结果
					VoteGroup mResult = (VoteGroup) msg.obj;
					LogUtils.getInstance().toast(mMainActivity,
							"结果发布了");
					LogUtils.getInstance().toast(mMainActivity,
							mResult.getM_questions().get(0).getM_nResultUser()+"人");
					LogUtils.getInstance().logD(mResult.getM_questions().get(0).getM_nResultUser()+"人");
					checkVoteGroupList(mResult);
					// 更新spinner
					mSpinnerAdapter.notifyDataSetChanged();
					mVoteCount.setText("共"+mVoteGroupList.size()+"份");
					Toast.makeText(mMainActivity,mResult.getM_questions().get(0).getM_nResultUser()+"人"
							, Toast.LENGTH_LONG).show();
					break;

				default:
					break;
			}

			Toast.makeText(mMainActivity, text, Toast.LENGTH_LONG).show();
		};
	};
	private Spinner mAllVote;
	private TextView mVoteCount;
	private ExpandableListView mShowResult;
	private ArrayAdapter<String> mSpinnerAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mMainActivity = (MyLiveActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		init();
		View view = initView();
		initData();
		return view;
	}

	// 初始化使用的rtsdk
	private void init() {
		mRtSdk = mMainActivity.getRtSdk();
		mRtSdk.setVoteCallback(new IVoteCallBackImpl());
	}

	// 初始化显示结果的界面
	private View initView() {
		View mView = View.inflate(mMainActivity, R.layout.vote_result, null);
		ll = (LinearLayout) mView.findViewById(R.id.vote_receiver_bottom_ly);
		rl = (RelativeLayout) mView.findViewById(R.id.rlToast);
		mAllVote = (Spinner) mView.findViewById(R.id.questionSP);
		mVoteCount = (TextView) mView.findViewById(R.id.vote_count_tv);
		mShowResult = (ExpandableListView) mView
				.findViewById(R.id.vote_receiver_lv);
		//没有投票结果时，不显示
		ll.setVisibility(View.GONE);
		return mView;
	}

	private void initData() {
		mSpinnerAdapter = new ArrayAdapter<>(mMainActivity,
				R.layout.spinner_item, R.id.tv, mVoteThemeList);

		mAllVote.setAdapter(mSpinnerAdapter);
		mAllVote.setOnItemSelectedListener(mOnItemSelectedListener);

	}

	// 检查发布后的结果
	private void checkVoteGroupList(VoteGroup mVoteGroup) {
		int m = 0;
		boolean isExsist = false;
		for (; m < mVoteGroupList.size(); m++) {
			if (mVoteGroup.getM_strId().equals(
					mVoteGroupList.get(m).getM_strId())) {
				isExsist = true;
				break;
			}
		}
		if (isExsist) {
			mVoteGroupList.set(m, mVoteGroup);
			mVoteThemeList.set(m, mVoteGroup.getM_strText());
		} else{
			mVoteGroupList.add(mVoteGroup);
			mVoteThemeList.add(mVoteGroup.getM_strText());
		}
	}

	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
								   int position, long id) {
			VoteGroupListAdapter mVoteGroupListAdapter=new VoteGroupListAdapter(mMainActivity);
			mVoteGroupListAdapter.setVoteGroup(mVoteGroupList.get(position), true);
			mShowResult.setAdapter(mVoteGroupListAdapter);
			setExpandableListViewStyle(mShowResult, mVoteGroupListAdapter);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};
	// 设置expandlistview展开
	private void setExpandableListViewStyle(ExpandableListView lvVoteQuestion,VoteGroupListAdapter adapter) {
		lvVoteQuestion.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
										int groupPosition, long id) {
				return true;
			}
		});
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			lvVoteQuestion.expandGroup(i);
		}
	}

	// 投票接口回调
	private class IVoteCallBackImpl extends MyIVoteCallBack {
		@Override
		public void onVoteAdd(VoteGroup voteGroup) {
			Message msg = handler.obtainMessage();
			msg.what = VOTE_ADD;
			msg.obj = voteGroup;
			msg.sendToTarget();
		}

		// 截至时，发送的通知
		@Override
		public void onVoteDeadline(VoteGroup voteGroup) {
			Message msg = handler.obtainMessage();
			msg.what = VOTE_DEADLINE;
			msg.obj = voteGroup;
			msg.sendToTarget();
		}

		@Override
		public void onVoteDel(VoteGroup voteGroup) {
			Message msg = handler.obtainMessage();
			msg.what = VOTE_DEL;
			msg.obj = voteGroup;
			msg.sendToTarget();

		}

		// 点击发布，发送的通知
		@Override
		public void onVotePublish(VoteGroup voteGroup) {
			Message msg = handler.obtainMessage();
			msg.what = VOTE_PUBLISH;
			msg.obj = voteGroup;
			msg.sendToTarget();
		}

		// 当服务器发布的时候，发送的通知
		@Override
		public void onVotePublishResult(VoteGroup voteGroup) {

			Message msg = handler.obtainMessage();
			msg.what = VOTE_PUBLISH_RESULT;
			msg.obj = voteGroup;
			msg.sendToTarget();
		}

		@Override
		public void onVoteSubmit(VoteGroup voteGroup) {
			Message msg = handler.obtainMessage();
			msg.what = VOTE_SUBMI;
			msg.obj = voteGroup;
			msg.sendToTarget();
		}
	}

}
