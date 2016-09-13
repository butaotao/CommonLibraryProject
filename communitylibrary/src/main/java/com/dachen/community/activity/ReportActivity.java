package com.dachen.community.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.community.R;
import com.dachen.community.contract.ReportContract;
import com.dachen.community.data.impl.remote.ReportRemoteImpl;
import com.dachen.community.presenters.ReportPrestener;
import com.dachen.community.views.GridRadioGroup;

import java.util.List;

/**
 * Created by pqixi on 2016/9/12 0012.
 * 举报页面
 */
public class ReportActivity extends BaseActivity implements View.OnClickListener, GridRadioGroup.OnCheckChanged, ReportContract.ReportView {

    protected TextView tvReportTitle;
    protected GridRadioGroup rgRadiogroups;
    protected EditText etDesription;
    protected Button btnReport;
    protected View vError;

    private ReportContract.ReportPresenter mPrestener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_report);
        new ReportPrestener(this, this, new ReportRemoteImpl());
        initView();

        mPrestener.initData(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPrestener.start();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_report) {

        } else if (view.getId() == R.id.v_error) {
            mPrestener.start();
        }
    }

    private void initView() {
        setTitle(getString(R.string.report_button));

        tvReportTitle = (TextView) findViewById(R.id.tv_report_title);
        rgRadiogroups = (GridRadioGroup) findViewById(R.id.rg_radiogroups);
        rgRadiogroups.setmListener(this);
        etDesription = (EditText) findViewById(R.id.et_desription);
        btnReport = (Button) findViewById(R.id.btn_report);
        vError = findViewById(R.id.v_error);
        vError.setVisibility(View.GONE);
        btnReport.setOnClickListener(ReportActivity.this);
        vError.setOnClickListener(ReportActivity.this);
    }


    /**
     * 控制按钮的点击状态
     */
    @Override
    public void onReport(boolean isSuccess, String massege) {
        //退出界面或者弹吐司
        if (isSuccess) {
            ToastUtil.showToast(this, "举报成功");
            finish();
        }
        ToastUtil.showToast(this, "举报失败:" + massege);
    }

    @Override
    public void refreshButtonStatus(boolean isEnable) {
        btnReport.setEnabled(isEnable);
    }

    /**
     * 设置举报的主题，可以支持点击多个地方
     *
     * @param builder
     */
    @Override
    public void setTopicText(SpannableStringBuilder builder) {
        tvReportTitle.setText(builder);
        tvReportTitle.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onReportType(boolean isSuccess, List<String> reportTypes) {
        vError.setVisibility(isSuccess ? View.GONE : View.VISIBLE);
        rgRadiogroups.setmSelects(reportTypes);
    }

    @Override
    public void onCheckedChanged(RadioButton checkButton, String text) {
        mPrestener.handleButtonStatus(rgRadiogroups.getCheckText());
    }

    @Override
    public void setPresenter(ReportContract.ReportPresenter presenter) {
        mPrestener = presenter;
    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void hideLoadingDialog() {

    }
}
