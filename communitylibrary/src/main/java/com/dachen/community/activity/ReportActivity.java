package com.dachen.community.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dachen.community.R;
import com.dachen.community.contract.ReportContract;
import com.dachen.community.data.impl.remote.ReportRemoteSource;
import com.dachen.community.presenters.ReportPrestener;
import com.dachen.community.views.GridRadioGroup;

/**
 * Created by pqixi on 2016/9/12 0012.
 * 举报页面
 */
public class ReportActivity extends BaseActivity implements View.OnClickListener, GridRadioGroup.OnCheckChanged, TextWatcher, ReportContract.ReportView {

    protected TextView tvReportTitle;
    protected GridRadioGroup rgRadiogroups;
    protected EditText etDesription;
    protected Button btnReport;

    private ReportPrestener mPrestener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_report);
        mPrestener = new ReportPrestener(this, new ReportRemoteSource());
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
        rgRadiogroups.setmListener(this);
        etDesription = (EditText) findViewById(R.id.et_desription);
        etDesription.addTextChangedListener(this);
        btnReport = (Button) findViewById(R.id.btn_report);
        btnReport.setOnClickListener(ReportActivity.this);

    }


    @Override
    public void onCheckedChanged(RadioButton checkButton, String text) {
        mPrestener.handleButtonStatus(rgRadiogroups.getCheckText(), etDesription.getText().toString().trim());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mPrestener.handleButtonStatus(rgRadiogroups.getCheckText(), etDesription.getText().toString().trim());
    }


    /**
     * 控制按钮的点击状态
     */
    @Override
    public void onReport(boolean isSuccess, String massege) {
        //退出界面或者弹吐司
        if (isSuccess) {

        }
    }

    @Override
    public void refreshButtonStatus(boolean isEnable) {
        btnReport.setEnabled(isEnable);
    }

    /**
     * 设置举报的主题，可以支持点击多个地方
     * @param builder
     */
    @Override
    public void setTopicText(SpannableStringBuilder builder) {
        tvReportTitle.setText(builder);
        tvReportTitle.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
