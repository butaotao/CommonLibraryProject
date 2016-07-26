package com.dachen.incomelibrary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.incomelibrary.R;
import com.dachen.incomelibrary.bean.AccountInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

public class AccountAdapter extends BaseAdapter {

	private Context context;
	private List<AccountInfo> accountInfos;
	
	public AccountAdapter(Context context, List<AccountInfo> accountInfos) {
		super();
		this.context = context;
		this.accountInfos = accountInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return accountInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return accountInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.bank_account_list, null);
			holder=new Holder();
			holder.headerImage=(ImageView)view.findViewById(R.id.bank_header);
			holder.bankName=(TextView)view.findViewById(R.id.bank_name);
			holder.bankDesc=(TextView)view.findViewById(R.id.bank_desc);
			holder.default_card=(TextView)view.findViewById(R.id.default_card);
			view.setTag(holder);
		}else{
			holder=(Holder)view.getTag();
		}
		AccountInfo accountInfo=accountInfos.get(arg0);
		if(accountInfo!=null){
			holder.accountInfo=accountInfo;
			DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheOnDisc(true)
						.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
						.cacheOnDisc(true)
						.showImageOnFail(R.drawable.defulat_bank)
						.bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(300)).build();
				ImageLoader.getInstance().displayImage(accountInfo.getBankIoc(),holder.headerImage,options);

//			switch (accountInfo.getBankId()) {
//			case ConstantsApp.GS_BANK:
//				holder.headerImage.setImageResource(R.drawable.gs);
//				break;
//			case ConstantsApp.GD_BANK:
//				holder.headerImage.setImageResource(R.drawable.gd);
//				break;
//			case ConstantsApp.GF_BANK:
//				holder.headerImage.setImageResource(R.drawable.gf);
//				break;
//			case ConstantsApp.MS_BANK:
//				holder.headerImage.setImageResource(R.drawable.ms);
//				break;
//			case ConstantsApp.JT_BANK:
//				holder.headerImage.setImageResource(R.drawable.jt);
//				break;
//			case ConstantsApp.PA_BANK:
//				holder.headerImage.setImageResource(R.drawable.pa);
//				break;
//			case ConstantsApp.ZS_BANK:
//				holder.headerImage.setImageResource(R.drawable.zs);
//				break;
//			case ConstantsApp.ZX_BANK:
//				holder.headerImage.setImageResource(R.drawable.zx);
//				break;
//			case ConstantsApp.YZ_BANK:
//				holder.headerImage.setImageResource(R.drawable.yz);
//				break;
//			case ConstantsApp.NY_BANK:
//				holder.headerImage.setImageResource(R.drawable.ny);
//				break;
//			case ConstantsApp.PF_BANK:
//				holder.headerImage.setImageResource(R.drawable.pf);
//				break;
//			case ConstantsApp.JS_BANK:
//				holder.headerImage.setImageResource(R.drawable.js);
//				break;
//			case ConstantsApp.XY_BANK:
//				holder.headerImage.setImageResource(R.drawable.xy);
//				break;
//			case ConstantsApp.ZG_BANK:
//				holder.headerImage.setImageResource(R.drawable.zg);
//				break;
//
//			default:
//				holder.headerImage.setImageResource(R.drawable.defulat_bank);
//				break;
//			}
			holder.bankName.setText(accountInfo.getBankName());
			holder.bankDesc.setText("尾号"+accountInfo.getBankNo().substring(accountInfo.getBankNo().length()-4, accountInfo.getBankNo().length()));

//			if (accountInfo.isIsDefault()) {
//				holder.default_card.setVisibility(View.VISIBLE);
//			} else {
//				holder.default_card.setVisibility(View.GONE);
//			}
		}
		return view;
	}

	
  public class Holder{
	  public AccountInfo accountInfo;
	  public ImageView  headerImage;
	  public TextView  bankName;
	  public TextView  bankDesc;
	  public TextView default_card;
  }
}
