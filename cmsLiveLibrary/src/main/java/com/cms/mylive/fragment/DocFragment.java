package com.cms.mylive.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cms.mylive.MyLiveActivity;
import com.cms.mylive.R;
import com.gensee.pdu.GSDocView;
import com.gensee.pdu.GSDocView.OnDocViewEventListener;

public class DocFragment extends Fragment {
	public static GSDocView mGSDocView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = View.inflate(getActivity(),R.layout.doc_fragment , null);
		mGSDocView=(GSDocView) view.findViewById(R.id.docView);
//		mGSDocView.showFillView();
		mGSDocView.setOnDocViewClickedListener(mOnDocViewEventListener);
		return view;
	}

	public GSDocView getGSDocView(){
		if(mGSDocView!=null)
			return mGSDocView;
		return
				null;
	}

	private OnDocViewEventListener mOnDocViewEventListener= new OnDocViewEventListener() {

		@Override
		public boolean onSingleClicked(GSDocView arg0) {
//			changeDocView();
			return false;
		}

		@Override
		public boolean onEndHDirection(GSDocView arg0, int arg1) {

			return false;
		}

		@Override
		public boolean onDoubleClicked(GSDocView arg0) {

			return false;
		}
	};
	private View view;

	private void changeDocView(){
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		//根据屏幕的宽高来设置横竖屏
		if (width > height) {
			if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		} else {
			if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
		int margin=0;
		if (width > height) {

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			lp.setMargins(margin, 0, margin, 0);
			((MyLiveActivity)getActivity()).getViewPager().setLayoutParams(lp);
		} else {

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			lp.setMargins(0, margin, 0, margin);
			((MyLiveActivity)getActivity()).getViewPager().setLayoutParams(lp);
		}
	}
}
