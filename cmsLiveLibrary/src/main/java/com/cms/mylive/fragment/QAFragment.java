package com.cms.mylive.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.cms.mylive.MyLiveActivity;
import com.cms.mylive.R;
import com.cms.mylive.adapter.QAListAdapter;
import com.cms.mylive.callbacks.MyIQACallback;
import com.gensee.qa.QaQuestion;
import com.gensee.room.RtSdk;

public class QAFragment extends Fragment {
	private MyLiveActivity mMainActivity;
	private RtSdk mRtSdk;
	private ImageView btnSend;
	private int LOAD_NEW_QA=250;

	private ListView lvQA;
	private QAListAdapter mQAListAdapter;
	private List<QaQuestion> mQaQuestionList;

	//处理子线程发送过来的消息 
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what==LOAD_NEW_QA){
				mQAListAdapter.setQaQuestionList(mQaQuestionList);
				lvQA.setSelection(mQAListAdapter.getCount()-1);
			}
		};
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mMainActivity = (MyLiveActivity) activity;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = initView(inflater);
		init();
		return view;
	}

	// 初始化控件
	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.qa_fragment, null);

		btnSend = (ImageView) view.findViewById(R.id.btnSend);
		lvQA = (ListView) view.findViewById(R.id.lvQA);
		final EditText et = (EditText) view.findViewById(R.id.chat_edittext);

		mQaQuestionList = new ArrayList<>();
		mQAListAdapter = new QAListAdapter(getActivity());
		mQAListAdapter.setQaQuestionList(mQaQuestionList);
		lvQA.setAdapter(mQAListAdapter);

		btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!TextUtils.isEmpty(et.getText().toString())) {
					mRtSdk.qaAddQuestion(et.getText().toString(), null);
					et.setText("");
				}
			}
		});
		return view;
	}

	//初始化使用的rtsdk
	private void init() {
		mRtSdk = mMainActivity.getRtSdk();
		mRtSdk.setQACallback(new IQACallbackImpl());

	}


	// 问答回调接口
	private class IQACallbackImpl extends MyIQACallback {

		@Override
		public void onQaQuestion(QaQuestion qaQuestion, int state) {
			if (QA_STATE_ANSWER == state || state == QA_STATE_QUESTION){
				mQaQuestionList.add(qaQuestion);
			}
			//通知更新数据
			handler.sendEmptyMessage(LOAD_NEW_QA);
		}
	}

}
