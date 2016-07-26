package com.cms.mylive.adapter;


import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> lists;

	public ViewPagerAdapter(FragmentManager fm,List<Fragment> lists) {
		super(fm);
		this.lists=lists;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists==null?0:lists.size();
	}

//	private List<Fragment> lists;
//	private Context context;
//
//	public ViewPagerAdapter(Context context,List<Fragment> lists){
//		this.context=context;
//		this.lists=lists;
//	}
//	
//	@Override
//	public int getCount() {
//		return lists==null?0:lists.size();
//	}
//
//	@Override
//	public boolean isViewFromObject(View arg0, Object arg1) {
//		// TODO Auto-generated method stub
//		return arg0==arg1;
//	}
//	
//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//		container.addView(lists.get(position));
//	}
//	
//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		container.removeView((View) object);
//	}


}
