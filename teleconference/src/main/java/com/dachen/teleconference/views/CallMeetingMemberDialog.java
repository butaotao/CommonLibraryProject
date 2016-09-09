package com.dachen.teleconference.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.teleconference.R;
import com.dachen.teleconference.constants.ImageLoaderConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 呼叫网络参会人和落地电话参会人Dialog
 * Created by TianWei on 2016/8/24.
 */
public class CallMeetingMemberDialog extends Dialog implements View.OnClickListener {

    private final Context mContext;
    private final String mName;
    private final String mHeadImageUrl;
    private final CallMeetingListener mListener;
    private final boolean mFlag;
    private ImageView mCloseIv;
    private ImageView mHeadImageIv;
    private TextView mNameTv;
    private RelativeLayout mPhoneCallLayout;
    private RelativeLayout mNetCallLayout;
    private View mDividerLineView;

    public CallMeetingMemberDialog(Context context, String name, String headImageUrl, CallMeetingListener listener,
                                   boolean flag) {
        super(context, R.style.dialog);
        mContext = context;
        mName = name;
        mHeadImageUrl = headImageUrl;
        mListener = listener;
        mFlag = flag;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_call_meeting_member);

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);

        mCloseIv = (ImageView) findViewById(R.id.close_iv);
        mHeadImageIv = (ImageView) findViewById(R.id.head_image_iv);
        mNameTv = (TextView) findViewById(R.id.name_tv);


        mPhoneCallLayout = (RelativeLayout) findViewById(R.id.phone_call_layout);
        mNetCallLayout = (RelativeLayout) findViewById(R.id.net_call_layout);
        mDividerLineView = findViewById(R.id.divider_line);
        if (mFlag) {
            mNetCallLayout.setVisibility(View.GONE);
            mDividerLineView.setVisibility(View.GONE);
        }

        mCloseIv.setOnClickListener(this);
        mPhoneCallLayout.setOnClickListener(this);
        mNetCallLayout.setOnClickListener(this);
        mNameTv.setText(mName);
        ImageLoader.getInstance().displayImage(mHeadImageUrl, mHeadImageIv, ImageLoaderConfig.mCircleImageOptions);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.close_iv) {
            dismiss();
        } else if (id == R.id.phone_call_layout) {
            mListener.onPhoneCall();
            dismiss();
        } else if (id == R.id.net_call_layout) {
            mListener.onNetCall();
            dismiss();
        }

    }

    public interface CallMeetingListener {
        void onNetCall();

        void onPhoneCall();
    }
}
