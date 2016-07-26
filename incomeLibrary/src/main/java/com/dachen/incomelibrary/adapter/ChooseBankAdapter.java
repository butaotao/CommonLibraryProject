package com.dachen.incomelibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dachen.incomelibrary.R;
import com.dachen.incomelibrary.bean.BankInfo;

import java.util.ArrayList;
import java.util.List;

public class ChooseBankAdapter extends BaseAdapter {
	
	private Context context;
	private List<BankInfo> bankInfos=new ArrayList<BankInfo>();
	
	
	public ChooseBankAdapter(Context context, List<BankInfo> bankInfos) {
		super();
		this.context = context;
		this.bankInfos = bankInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bankInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return bankInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.choose_bank_list, null);
			viewHolder=new ViewHolder();
			viewHolder.bankName=(TextView)view.findViewById(R.id.bank_name);
			view.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)view.getTag();
		}
		BankInfo bankInfo=bankInfos.get(arg0);
		if(bankInfo!=null){
			viewHolder.bankName.setText(bankInfo.getBankName());
			viewHolder.bankInfo=bankInfo;
		}
		return view;
	}

	public class ViewHolder{
		public  BankInfo bankInfo;
		public  TextView bankName;
	}
}
