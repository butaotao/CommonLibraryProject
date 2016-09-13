package com.dachen.community.contract;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;

import com.dachen.community.BasePresenter;
import com.dachen.community.BaseView;

import java.util.List;

/**
 * Created by pqixi on 2016/9/12 0012.
 */
public interface ReportContract {

    interface ReportView extends BaseView<ReportPresenter> {
        void onReport(boolean isSuccess, String massege);

        void refreshButtonStatus(boolean isEnable);

        void setTopicText(SpannableStringBuilder builder);

        void onReportType(boolean isSuccess, List<String> reportTypes);//获取举报类型
    }

    interface ReportPresenter extends BasePresenter {

        void report(int type, String desc);

        void handleButtonStatus(String mSelectText);

        void setMulitClickText(Context context, String name, String topic);

        void initData(Intent intent);
    }
}
