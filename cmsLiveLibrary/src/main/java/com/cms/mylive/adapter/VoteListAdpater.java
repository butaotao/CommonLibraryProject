package com.cms.mylive.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cms.mylive.R;
import com.gensee.vote.VoteAnswer;
import com.gensee.vote.VoteGroup;
import com.gensee.vote.VoteQuestion;

public class VoteListAdpater extends BaseAdapter {

	private Context context;
	private VoteGroup mVoteGroup;
	private List<VoteQuestion> mVoteQuestion;
	//保存用户的答案
	private VoteGroup mAnwserVoteGroup=new VoteGroup();

	public VoteListAdpater(Context context) {
		this.context = context;
	}

	public void setVoteList(VoteGroup mVoteGroup) {
		this.mVoteGroup = mVoteGroup;
		getQuestList();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mVoteQuestion == null ? 0 : mVoteQuestion.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(context, R.layout.vote_list_question, null);

		TextView tvQuestion = (TextView) convertView
				.findViewById(R.id.tvQuestion);
		LinearLayout llAnwser = (LinearLayout) convertView
				.findViewById(R.id.llAnwser);

		// 问题
		VoteQuestion voteQuestion = mVoteQuestion.get(position);
		String questionText = voteQuestion.getM_strText();
		tvQuestion.setText(position + 1 + ". " + questionText + "【" + "多选"
				+ "】");
		tvQuestion.setTextColor(Color.parseColor("#B6322C"));

		char location=65;
		// 判断是多选还是单选
		if (voteQuestion.getM_strType().equals("single")) {
			List<VoteAnswer> anwsersList = voteQuestion.getM_answers();
			RadioGroup rg = new RadioGroup(context);
			for (VoteAnswer i : anwsersList) {
				RadioButton rb = new RadioButton(context);
				// rb.setButtonDrawable(R.drawable.check_selector);
				rb.setText((location++)+":"+i.getM_strText());
				// 把答案生成，直接添加到布局中
				rg.addView(rb);

			}
			llAnwser.addView(rg);
		} else if (voteQuestion.getM_strType().equals("text")) {

		} else {
			List<VoteAnswer> anwsersList = voteQuestion.getM_answers();
			for (VoteAnswer i : anwsersList) {
				CheckBox cb = new CheckBox(context);
//				cb.setButtonDrawable(R.drawable.check_selector);
				cb.setText((location++)+":"+i.getM_strText());
				cb.setFocusable(false);
				cb.setFocusableInTouchMode(false);
//				cb.setClickable(false);
				// 把答案生成，直接添加到布局中
				llAnwser.addView(cb);
			}
		}

		return convertView;
	}

	private void getQuestList() {
		this.mVoteQuestion = mVoteGroup.getM_questions();
	}

	//得到选择的答案
	public VoteGroup getVoteGroup(){

		return mAnwserVoteGroup;
	}
}
