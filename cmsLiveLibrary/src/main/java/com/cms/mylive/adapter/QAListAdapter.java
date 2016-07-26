package com.cms.mylive.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cms.mylive.R;
import com.cms.mylive.utils.DateUtils;
import com.gensee.qa.QaAnswer;
import com.gensee.qa.QaQuestion;

public class QAListAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<QaQuestion> mQaQuestionList;
	private List<QaAnswer> mQaAnswerList;
	private Context context;
	private RelativeLayout relAnswer;

	public QAListAdapter(Context context) {
		this.context = context;
	}

	public void setQaQuestionList(List<QaQuestion> mQaQuestionList) {
		this.mQaQuestionList = mQaQuestionList;
		// mQaAnswerList=mQaQuestionList.
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mQaQuestionList.size();
	}

	@Override
	public Object getItem(int position) {
		return mQaQuestionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
//		if(convertView==null){
			holder=new ViewHolder();
			convertView=View.inflate(context, R.layout.gs_qa_item_layout, null);
			
			holder.tvQContent=(TextView) convertView.findViewById(R.id.txtQContent);
			holder.tvQUser=(TextView) convertView.findViewById(R.id.txtQUser);
			holder.tvQTime=(TextView) convertView.findViewById(R.id.txtQTime);
			
			
			holder.txtAContent=(TextView) convertView.findViewById(R.id.txtAContent);
			holder.txtAUser=(TextView) convertView.findViewById(R.id.txtAUser);
			holder.txtATime=(TextView) convertView.findViewById(R.id.txtATime);
			holder.txtATag=(TextView) convertView.findViewById(R.id.txtATime);
			
			relAnswer = (RelativeLayout) convertView.findViewById(R.id.relAnswer);
			
//			convertView.setTag(holder);
//		}else{
//			holder=(ViewHolder) convertView.getTag();
//		}
		
		holder.tvQContent.setText(mQaQuestionList.get(position).getStrQuestionContent());
		holder.tvQUser.setText(mQaQuestionList.get(position).getStrQuestionOwnerName());
		holder.tvQTime.setText("("+DateUtils.longToDate(mQaQuestionList.get(position).getDwQuestionTime(), "HH:mm:ss")+")");
		
		if(mQaQuestionList.get(position).getQaAnswerList().size()>0){
			int size = mQaQuestionList.get(position).getQaAnswerList().size();
			QaAnswer qaAnswer = mQaQuestionList.get(position).getQaAnswerList().get(size-1);
			
			for(int i=0;i<size;i++){
				Log.i("HelloWorld",  mQaQuestionList.get(position).getQaAnswerList().get(i).getStrAnswerContent());
			}
			holder.txtAContent.setVisibility(View.VISIBLE);
			holder.txtAUser.setVisibility(View.VISIBLE);
			holder.txtATime.setVisibility(View.VISIBLE);
			holder.txtATag.setVisibility(View.VISIBLE);
			relAnswer.setVisibility(View.VISIBLE);
			
			holder.txtAContent.setText(qaAnswer.getStrAnswerContent());
			holder.txtATime.setText("("+DateUtils.longToDate(qaAnswer.getDwAnswerTime(), "HH:mm:ss")+")");
			holder.txtAUser.setText(qaAnswer.getStrAnswerOwnerName());
			
		}else{
			holder.txtAContent.setVisibility(View.GONE);
			holder.txtAUser.setVisibility(View.GONE);
			holder.txtATime.setVisibility(View.GONE);
			holder.txtATag.setVisibility(View.GONE);
			relAnswer.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView tvQContent;
		TextView tvQUser;
		TextView tvQTime;
		TextView txtAContent;
		TextView txtAUser;
		TextView txtATime;
		TextView txtATag;
	}

}
