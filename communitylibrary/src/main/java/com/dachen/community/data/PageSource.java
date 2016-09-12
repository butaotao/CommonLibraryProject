package com.dachen.community.data;

import com.dachen.community.data.bean.PageData;

import java.util.List;

/**
 * Created by RnMonkey on 16/9/11.
 */

public interface PageSource {
    interface LoadPageCallBack{
        void callback(List<PageData> datas);
    }

    void getPageDatas(int page);
}
