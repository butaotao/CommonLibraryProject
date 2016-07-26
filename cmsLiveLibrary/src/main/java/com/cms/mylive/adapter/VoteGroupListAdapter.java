package com.cms.mylive.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cms.mylive.R;
import com.gensee.vote.VoteAnswer;
import com.gensee.vote.VoteGroup;
import com.gensee.vote.VoteQuestion;
import com.gensee.vote.VoteQuestion.VoteQuestionType;

public class VoteGroupListAdapter extends BaseExpandableListAdapter {

	private VoteGroup mVoteGroup;
	private Context context;
	private List<VoteQuestion> mVoteQuestion;
	private boolean isPublishResult = false; // 是否是显示发布结果

	public VoteGroupListAdapter(Context context) {
		this.context = context;

	}

	public void setVoteGroup(VoteGroup mVoteGroup, boolean isPublishResult) {
		this.mVoteGroup = mVoteGroup;
		mVoteQuestion = mVoteGroup.getM_questions();
		this.isPublishResult=isPublishResult;
		notifyDataSetChanged();
	}

	public VoteGroup getVoteGroup() {
		if (mVoteGroup == null)
			return null;
		return mVoteGroup;
	}

	@Override
	public int getGroupCount() {

		return mVoteGroup == null ? 0 : mVoteQuestion.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mVoteGroup == null ? 0 : mVoteQuestion.get(groupPosition)
				.getM_answers().size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return mVoteQuestion.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		return mVoteQuestion.get(groupPosition).getM_answers()
				.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public boolean hasStableIds() {

		return false;
	}



	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
		QuestionHolder mQuestionHolder = null;
		if (convertView == null) {
			convertView = View
					.inflate(context, R.layout.vote_commit_item, null);
			mQuestionHolder = new QuestionHolder(convertView);
			convertView.setTag(mQuestionHolder);
		} else {
			mQuestionHolder = (QuestionHolder) convertView.getTag();
		}
		mQuestionHolder.init(mVoteQuestion.get(groupPosition), groupPosition);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		AnswerHolder mAnswerHolder;
		if (convertView == null) {
			convertView = View
					.inflate(context, R.layout.vote_anwser_item, null);
			mAnswerHolder = new AnswerHolder(convertView);
			convertView.setTag(mAnswerHolder);
		} else {
			mAnswerHolder = (AnswerHolder) convertView.getTag();
		}
		mAnswerHolder.init(
				mVoteQuestion.get(groupPosition),
				mVoteQuestion.get(groupPosition).getM_answers()
						.get(childPosition), childPosition);

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return false;
	}

	// 问答题目holder
	private class QuestionHolder {
		TextView tvTitle;

		public QuestionHolder(View view) {
			tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		}

		public void init(VoteQuestion voteQuestion, int groupPosition) {
			String type = "";
			if (voteQuestion.getM_strType()
					.equals(VoteQuestionType.SINGLE_TYPE)) {
				type = "单选";
			} else if (voteQuestion.getM_strType().equals(
					VoteQuestionType.MULTI_TYPE)) {
				type = "多选";
			} else {
				type = "问答题";
			}

			tvTitle.setText(groupPosition + 1 + ". "
					+ voteQuestion.getM_strText() + "【" + type + "】");

		}

	}

	private class AnswerHolder {
		CheckBox mCheckBox;
		RadioButton mRadioButton;
		TextView mTextView;
		LinearLayout mLinearLayout;
		//		ShowScoreView mShowScoreView;
		TextView mShowVoteCount;
		TextView tvAllUserSize;
		public AnswerHolder(View view) {
			mCheckBox = (CheckBox) view.findViewById(R.id.cbAnwser);
			mRadioButton = (RadioButton) view.findViewById(R.id.rbAnwser);
			mTextView = (TextView) view.findViewById(R.id.tvAnwser);
//			mShowScoreView=(ShowScoreView) view.findViewById(R.id.showScore);
			mShowVoteCount=(TextView) view.findViewById(R.id.showScore);
			tvAllUserSize=(TextView) view.findViewById(R.id.tvAllUserSize);
		}

		public void init(final VoteQuestion voteQuestion,
						 final VoteAnswer voteAnswer, final int childPosition) {

			int loaction = 65;
			if (!isPublishResult) {
				if (voteQuestion.getM_strType().equals(
						VoteQuestionType.SINGLE_TYPE)) {
					mCheckBox.setVisibility(View.GONE);
					mRadioButton.setVisibility(View.VISIBLE);
					mRadioButton.setChecked(voteAnswer.isM_bChoose());

				} else if (voteQuestion.getM_strType().equals(
						VoteQuestionType.MULTI_TYPE)) {
					mCheckBox.setVisibility(View.VISIBLE);
					mRadioButton.setVisibility(View.GONE);
					mCheckBox.setChecked(voteAnswer.isM_bChoose());
				}
				mShowVoteCount.setVisibility(View.GONE);
			}else{
				mCheckBox.setVisibility(View.GONE);
				mRadioButton.setVisibility(View.GONE);

				mShowVoteCount.setVisibility(View.VISIBLE);
				mShowVoteCount.setText(voteAnswer.getUsersSize()+"票");

				if(childPosition==voteQuestion.getM_answers().size()-1){
					tvAllUserSize.setVisibility(View.VISIBLE);
					tvAllUserSize.setText("共有("+voteQuestion.getUsersSize()+")人参与");
				}else{
					tvAllUserSize.setVisibility(View.GONE);
				}
//				mShowScoreView.setProgress(voteAnswer.getM_nResultUser(), voteQuestion.getM_nResultUser());
			}

			mTextView.setText((char) (loaction + childPosition) + ". "
					+ voteAnswer.getM_strText());

			mCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					voteAnswer.setM_bChoose(mCheckBox.isChecked());
				}
			});

			mRadioButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (VoteAnswer tmpAnswer : voteQuestion.getM_answers()) {
						if (tmpAnswer.isM_bChoose()) {
							tmpAnswer.setM_bChoose(false);
						}
					}
//					Toast.makeText(context, "点击了" + childPosition, 0).show();
					voteAnswer.setM_bChoose(true);
					notifyDataSetChanged();
				}
			});

		}
	}

}
