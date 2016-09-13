package com.dachen.community.presenters;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.community.R;
import com.dachen.community.contract.ReportContract;
import com.dachen.community.data.ReportSource;
import com.dachen.community.data.requests.ReportRequest;
import com.dachen.community.data.results.ReportResult;

import java.util.List;

/**
 * Created by pqixi on 2016/9/12 0012.
 */
public class ReportPrestener implements ReportContract.ReportPresenter {
    private ReportContract.ReportView mView;
    private ReportSource mSource;
    private List<ReportResult.Type> mReportTypes;

    private Context context;
    private String topicId;
    private Integer userId;

    private String userName;
    private String topicTitle;

    public ReportPrestener(Context context, ReportContract.ReportView mView, ReportSource mSource) {
        this.context = context;
        this.mView = mView;
        this.mSource = mSource;

        this.mView.setPresenter(this);
    }

    @Override
    public void report(int type, String desc) {
        ReportRequest request = new ReportRequest();
        request.setDesc(desc);
        request.setTopicId(topicId);
        request.setUserId(userId);
        request.setType(type);

        mView.showLoadingDialog();
        mSource.report(request, new ReportSource.CallBack() {
            @Override
            public void onCallBack(ReportResult result) {

                mView.hideLoadingDialog();
                if (result == null) {
                    mView.onReport(false, "net is not vailue");
                    return;
                }
                mView.onReport(result.isSuccess(), result.getResultMsg());
            }
        });

    }

    @Override
    public void handleButtonStatus(String mSelectText) {
        boolean isEnable = !TextUtils.isEmpty(mSelectText);
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
    public void initData(Intent intent) {
        if (intent != null) {
            userName = intent.getStringExtra("userName");
            topicTitle = intent.getStringExtra("topicTitle");
            setMulitClickText(context, userName, topicTitle);

            topicId = intent.getStringExtra("topicId");
            userId = intent.getIntExtra("userId", 0);
        }
    }

    @Override
    public void start() {
        if (mReportTypes == null) {//请求网络数据
            final ReportRequest request = new ReportRequest();

            mView.showLoadingDialog();
            mSource.getReportType(request, new ReportSource.CallBack() {
                @Override
                public void onCallBack(ReportResult result) {
                    mView.hideLoadingDialog();

                    if (result != null && result.isSuccess()) {
                        mReportTypes = result.getTypes();
                        mView.onReportType(true, result.getTypeStringList());
                        return;
                    }
                    mView.onReportType(false, null);
                }
            });
        }
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
