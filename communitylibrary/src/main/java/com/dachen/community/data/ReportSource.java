package com.dachen.community.data;

import com.dachen.community.data.requests.ReportRequest;
import com.dachen.community.data.results.ReportResult;

/**
 * Created by pqixi on 2016/9/12 0012.
 */
public interface ReportSource {

    interface CallBack {
        void onCallBack(ReportResult result);
    }

    void report(ReportRequest request, CallBack callBack);

    void getReportType(ReportRequest request, CallBack callBack);
}
