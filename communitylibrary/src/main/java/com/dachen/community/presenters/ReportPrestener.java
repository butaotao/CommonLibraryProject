package com.dachen.community.presenters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.dachen.common.http2.result.BaseResult;
import com.dachen.common.utils.ToastUtil;
import com.dachen.community.R;
import com.dachen.community.contract.ReportContract;
import com.dachen.community.data.ReportSource;
import com.dachen.community.data.bean.ReportRequest;

/**
 * Created by pqixi on 2016/9/12 0012.
 */
public class ReportPrestener implements ReportContract.ReportPresenter, ReportSource.CallBack {
    private ReportContract.ReportView mView;

    private ReportSource mSource;

    public ReportPrestener(ReportContract.ReportView mView, ReportSource mSource) {
        this.mView = mView;
        this.mSource = mSource;
    }

    @Override
    public void report(int type, String desc) {
        ReportRequest request = new ReportRequest();
        mSource.report(request, this);
    }

    @Override
    public void handleButtonStatus(String mSelectText, String desc) {
        boolean isEnable = !TextUtils.isEmpty(mSelectText) && !TextUtils.isEmpty(desc);
        mView.refreshButtonStatus(isEnable);
    }

    @Override
    public void setMulitClickText(Context context, String name, String topic) {

        String s = context.getString(R.string.report_title_text, name, topic);
        SpannableStringBuilder builder = new SpannableStringBuilder(s);
        int start = s.indexOf(name);
        builder.setSpan(new Clickable(name, builder), start, start + name.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

        start = s.indexOf(topic);
        builder.setSpan(new Clickable(topic, builder), start, start + topic.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

        mView.setTopicText(builder);
    }

    @Override
    public void start() {

    }

    @Override
    public void onCallBack(BaseResult result) {
        if (result == null) {
            mView.onReport(false, "net is not vailue");
            return;
        }
        mView.onReport(result.isSuccess(), result.getResultMsg());
    }


    /**
     * textView分节点击事件
     */
    private class Clickable extends ClickableSpan {
        private String text;
        private SpannableStringBuilder stringBuilder;

        public Clickable(String text, SpannableStringBuilder stringBuilder) {
            this.stringBuilder = stringBuilder;
            this.text = text;
        }

        /**
         * Makes the text underlined and in the link color.
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            ToastUtil.showToast(widget.getContext(), text);
            ((TextView) widget).setText(stringBuilder);
        }
    }
}
