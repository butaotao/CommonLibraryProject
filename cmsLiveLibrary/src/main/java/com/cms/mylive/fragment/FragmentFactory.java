package com.cms.mylive.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

public class FragmentFactory {

	public static Fragment createFragment(int index) {
		Fragment mFragment = null;
		switch (index) {
		case 0:
			mFragment = new DocFragment();
			break;
		case 1:
			mFragment = new ChatFragment();
			break;
		case 2:
			mFragment = new QAFragment();
			break;
		case 3:
			mFragment = new VoteFragment();
			break;
		}
		return mFragment;
	}
}
