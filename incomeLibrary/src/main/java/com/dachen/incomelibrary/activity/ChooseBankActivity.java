package com.dachen.incomelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.incomelibrary.R;
import com.dachen.incomelibrary.adapter.ChooseBankAdapter;
import com.dachen.incomelibrary.bean.BankInfo;
import com.dachen.incomelibrary.http.HttpCommClient;
import com.dachen.incomelibrary.utils.ConstantsApp;
import com.dachen.incomelibrary.utils.Logger;
import com.dachen.incomelibrary.utils.UIHelper;
import com.dachen.incomelibrary.utils.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class ChooseBankActivity extends BaseActivity implements OnClickListener{
	
	private TextView back;
	private ListView choose_bank_lv;
	private List<BankInfo> bankInfos=new ArrayList<BankInfo>();
	private ChooseBankAdapter chooseBankAdapter;
	
	protected Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantsApp.HANDLER_GET_BANK_INFO:
				Logger.d("yehj", "HANDLER_GET_BANK_INFO");
				if(mDialog!=null&&mDialog.isShowing()){
					mDialog.dismiss();
				}
				if(msg.arg1==1){
					if(msg.obj!=null&&msg.obj instanceof List){
						bankInfos.clear();
						bankInfos.addAll((List<BankInfo>)msg.obj);
						chooseBankAdapter.notifyDataSetChanged();
					}
				}else{
					UIHelper.ToastMessage(ChooseBankActivity.this, (String)msg.obj);
				}
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_bank);
		initView();
		getData();
	}
	
	private void initView(){
		back=(TextView)findViewById(R.id.back);
		choose_bank_lv=(ListView)findViewById(R.id.choose_bank_lv);
		chooseBankAdapter=new ChooseBankAdapter(this,bankInfos);
		choose_bank_lv.setAdapter(chooseBankAdapter);
		back.setOnClickListener(this);
		choose_bank_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				if(view.getTag()==null||!(view.getTag() instanceof ChooseBankAdapter.ViewHolder)){
					return ;
				}
				ChooseBankAdapter.ViewHolder viewHolder=(ChooseBankAdapter.ViewHolder)view.getTag();
				if(viewHolder.bankInfo==null){
					return ;
				}
				Intent intent=new Intent();
				intent.putExtra(ConstantsApp.BANK_INFO,viewHolder.bankInfo);
				setResult(ConstantsApp.GOTO_CHOOSE_BANK_RESULT, intent);
				finish();
			}
		});
		
	}

	private void  getData(){
		if(mDialog!=null){
			mDialog.show();
		}
		HttpCommClient.getInstance().queryBankInfo(context, handler, ConstantsApp.HANDLER_GET_BANK_INFO, UserInfo.getInstance
				(context).getToken());
	}
	@Override
	public void onClick(View view) {

		if(view.getId()==R.id.back){
			finish();
		}
		
	}
	
}
