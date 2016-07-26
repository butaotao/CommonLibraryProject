package com.cms.mylive.view;

import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.mylive.MyLiveActivity;
import com.cms.mylive.R;
import com.cms.mylive.adapter.VoteGroupListAdapter;
import com.gensee.room.RtSdk;
import com.gensee.taskret.OnTaskRet;
import com.gensee.vote.VoteGroup;

public class MyPopupWindow {

	private RtSdk mRtSdk;
	private MyLiveActivity mMainActivity;

	public MyPopupWindow(MyLiveActivity mMainActivity, RtSdk mRtSdk) {
		this.mMainActivity = mMainActivity;
		this.mRtSdk = mRtSdk;
	}

	public void show(VoteGroup mVoteGroup, List<VoteGroup> mVoteGroupList) {
		View mView = View.inflate(mMainActivity, R.layout.vote_layout, null);

		TextView tvPopupVoteCount = (TextView) mView
				.findViewById(R.id.vote_count_tv);
		TextView tvPopupSelect = (TextView) mView.findViewById(R.id.select_tv);
		final Button btnSubmit = (Button) mView
				.findViewById(R.id.vote_receiver_commit_btn);
		LinearLayout llSubmit = (LinearLayout) mView
				.findViewById(R.id.llSubmiit);
		ExpandableListView mPopupVoteListView = (ExpandableListView) mView
				.findViewById(R.id.vote_receiver_lv);

		final VoteGroupListAdapter adapter = new VoteGroupListAdapter(
				mMainActivity);
		final PopupWindow mPopupWindow = new PopupWindow(mView,
				LayoutParams.MATCH_PARENT, mMainActivity.getPopupWindowHeight());
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adapter.getVoteGroup() != null)
					mRtSdk.voteSubmit(adapter.getVoteGroup(), MyOnTaskRet);
				btnSubmit.setSelected(false);
				btnSubmit.setEnabled(false);

				// 关闭popoupwindow
				mPopupWindow.dismiss();

			}
		});

		// 投票主题
		String themeText = mVoteGroup.getM_strText();
		// 设置是否可以没有投票就退出,如果是force这必须投票
		boolean isForce = mVoteGroup.isM_bForce();
		tvPopupSelect.setEnabled(!isForce);
		mPopupWindow.setFocusable(!isForce);

		tvPopupSelect.setText(themeText);
		tvPopupVoteCount.setText(mVoteGroupList.size() + "份");

		// 问答内容
		adapter.setVoteGroup(mVoteGroup, false);
		mPopupVoteListView.setAdapter(adapter);
		// 设置group不可点击
		mPopupVoteListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
										int groupPosition, long id) {
				return true;
			}
		});
		expandListView(mPopupVoteListView, adapter);

		View mDecorView = mMainActivity.getWindow().getDecorView();
		mPopupWindow.showAtLocation(mDecorView, Gravity.BOTTOM | Gravity.LEFT,
				0, 0);
	}

	private OnTaskRet MyOnTaskRet = new OnTaskRet() {

		@Override
		public void onTaskRet(boolean arg0, int arg1, String arg2) {
			mMainActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(mMainActivity, "你提交了", 0).show();
				}
			});
		}
	};

	// 设置expandlistview展开
	private void expandListView(ExpandableListView lvVoteQuestion,
								VoteGroupListAdapter adapter) {
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			lvVoteQuestion.expandGroup(i);
		}
	}
}
