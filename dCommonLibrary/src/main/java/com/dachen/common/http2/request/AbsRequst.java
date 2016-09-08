package com.dachen.common.http2.request;

import java.util.Map;

/**
 * Created by pqixi on 2016/9/8 0008.
 */
public interface AbsRequst {

    /**
     * 将reque里面的字段和值转成对应的Map
     *
     * @return
     */
    Map<String, String> toMap();
}
