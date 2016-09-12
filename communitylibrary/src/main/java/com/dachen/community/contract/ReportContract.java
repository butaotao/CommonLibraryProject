package com.dachen.community.contract;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.dachen.community.BasePresenter;

/**
 * Created by pqixi on 2016/9/12 0012.
 */
public interface ReportContract {

    interface ReportView {
        void onReport(boolean isSuccess, String massege);

        void refreshButtonStatus(boolean isEnable);

        void setTopicText(SpannableStringBuilder builder);
    }

    interface ReportPresenter extends BasePresenter {

        void report(int type, String desc);

        void handleButtonStatus(String mSelectText, String desc);

        void setMulitClickText(Context context,String name, String topic);

    }
}
