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
import com.dachen.teleconference.bean.MeetingRole;
import com.dachen.teleconference.constants.ImageLoaderConfig;
import com.dachen.teleconference.utils.MeetingInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 正在开会时收到其他会议邀请时的弹窗
 * Created by TianWei on 2016/9/5.
 */
public class OtherMeetingIncomingDialog extends Dialog implements View.OnClickListener {
    private final OtherMeetingIncomingListener mListener;
    private final Context mContext;
    private ImageView mHeadImageIv;
    private TextView mNameTv;
    private TextView mWarningTv;
    private TextView mIgnoreTv;
    private TextView mAnswerPhoneTv;
    private final String mName;
    private final String mHeadImageUrl;

    public OtherMeetingIncomingDialog(Context context, String name, String headImageUrl, OtherMeetingIncomingListener listener) {
        super(context, R.style.dialog);
        mContext = context;
        mName = name;
        mHeadImageUrl = headImageUrl;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_other_meeting_incoming);

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);


        mHeadImageIv = (ImageView) findViewById(R.id.head_image_iv);
        mNameTv = (TextView) findViewById(R.id.name_tv);
        mWarningTv = (TextView) findViewById(R.id.warning_text);
        mIgnoreTv = (TextView) findViewById(R.id.ignore_tv);
        mAnswerPhoneTv = (TextView) findViewById(R.id.answer_the_phone_tv);

        mIgnoreTv.setOnClickListener(this);
        mAnswerPhoneTv.setOnClickListener(this);

        mNameTv.setText(mName);
        ImageLoader.getInstance().displayImage(mHeadImageUrl, mHeadImageIv, ImageLoaderConfig.mCircleImageOptions);

        if (MeetingInfo.getInstance(mContext).getMeetingRole() == MeetingRole.Sponsor) {
            mWarningTv.setText(mContext.getString(R.string.warning_text));
            mAnswerPhoneTv.setText(mContext.getString(R.string.answer_the_phone_text));
        } else {
            mWarningTv.setText(mContext.getString(R.string.warning_text_simple));
            mAnswerPhoneTv.setText(mContext.getString(R.string.answer_the_phone_text_simple));
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.ignore_tv) {
            mListener.onIgnore();
        } else if (id == R.id.answer_the_phone_tv) {
            mListener.onAnswerThePhone();
        }

    }

    public interface OtherMeetingIncomingListener {
        void onAnswerThePhone();

        void onIgnore();
    }
}
