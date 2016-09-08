package com.dachen.common.http2.request;

import com.dachen.common.utils.Logger;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pqixi on 2016/9/8 0008.
 */
public class BaseRequest implements AbsRequst, Serializable {
    /**
     * 默认将该对象的字段转为map对象
     *
     * @return
     */
    @Override
    public Map<String, String> toMap() {
        Class<? extends BaseRequest> aClass = this.getClass();
        Field[] fields = aClass.getDeclaredFields();
        if (fields != null) {
            HashMap<String, String> map = new HashMap<>();
            for (Field field : fields) {
                if (field == null) {
                    continue;
                }
                try {
                    Object o = field.get(this);

                    field.setAccessible(true);
                    String key = field.getName();
                    if (o instanceof String) {
                        map.put(key, o.toString());
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            Logger.d("Monkey", "map = " + map);
            return map;
        }
        return null;
    }
}
