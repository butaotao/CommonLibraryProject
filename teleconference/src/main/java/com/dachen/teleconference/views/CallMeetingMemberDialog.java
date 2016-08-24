package com.dachen.teleconference.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.teleconference.R;

/**
 * 呼叫网络参会人和落地电话参会人Dialog
 * Created by TianWei on 2016/8/24.
 */
public class CallMeetingMemberDialog extends Dialog implements View.OnClickListener {

    private ImageView mCloseIv;
    private ImageView mHeadImageIv;
    private TextView mNameTv;
    private TextView mPhoneCallTv;
    private TextView mNetCallTv;

    public CallMeetingMemberDialog(Context context) {
        super(context,R.style.dialog);
    }

    public CallMeetingMemberDialog(Context context, int theme) {
        super(context, theme);
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
        mPhoneCallTv = (TextView) findViewById(R.id.phone_call_tv);
        mNetCallTv = (TextView) findViewById(R.id.net_call_tv);

        mCloseIv.setOnClickListener(this);
        mPhoneCallTv.setOnClickListener(this);
        mNetCallTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.close_iv) {
            dismiss();
        } else if (id == R.id.phone_call_tv) {

        } else if (id == R.id.net_call_tv) {

        }

    }
}
