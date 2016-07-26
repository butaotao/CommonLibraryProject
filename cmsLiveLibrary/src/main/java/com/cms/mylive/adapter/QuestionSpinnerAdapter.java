package com.cms.mylive.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cms.mylive.R;
import com.gensee.vote.VoteGroup;

public class QuestionSpinnerAdapter extends BaseAdapter {

	private Context context;
	private List<VoteGroup> mVoteGroupList;

	public QuestionSpinnerAdapter(Context context){
		this.context=context;
	}
	public void setVoteGroup(List<VoteGroup> mVoteGroupList){
		this.mVoteGroupList=mVoteGroupList;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mVoteGroupList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mVoteGroupList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.spinner_item, null);
			holder=new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.init(mVoteGroupList.get(position),position);
		return convertView;
	}

	private class ViewHolder{
		TextView tv;
		public ViewHolder(View convertView) {
			tv=(TextView) convertView.findViewById(R.id.tv);
		}
		public void init(VoteGroup voteGroup, int position) {
			tv.setText(voteGroup.getM_strText());
		}
		

	}
}
