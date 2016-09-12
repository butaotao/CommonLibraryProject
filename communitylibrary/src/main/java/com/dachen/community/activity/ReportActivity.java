package com.dachen.community.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.community.R;
import com.dachen.community.views.GridRadioGroup;

/**
 * Created by pqixi on 2016/9/12 0012.
 * 举报页面
 */
public class ReportActivity extends BaseActivity implements View.OnClickListener {

    protected TextView tvReportTitle;
    protected GridRadioGroup rgRadiogroups;
    protected EditText etDesription;
    protected Button btnReport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_report);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_report) {

        }
    }

    private void initView() {
        setTitle(getString(R.string.report_button));

        tvReportTitle = (TextView) findViewById(R.id.tv_report_title);
        rgRadiogroups = (GridRadioGroup) findViewById(R.id.rg_radiogroups);
        etDesription = (EditText) findViewById(R.id.et_desription);
        btnReport = (Button) findViewById(R.id.btn_report);
        btnReport.setOnClickListener(ReportActivity.this);

    }

    public void setTitleText(String name, String topical) {
        String s = getString(R.string.report_title_text, name, topical);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(s);
        int start = s.indexOf(name);
        stringBuilder.setSpan(new Clickable(name, stringBuilder), start, start + name.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

        start = s.indexOf(topical);
        stringBuilder.setSpan(new Clickable(topical, stringBuilder), start, start + topical.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvReportTitle.setText(stringBuilder);
        tvReportTitle.setMovementMethod(LinkMovementMethod.getInstance());
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
            ToastUtil.showToast(getBaseContext(), text);
            ((TextView) widget).setText(stringBuilder);
        }
    }
}
