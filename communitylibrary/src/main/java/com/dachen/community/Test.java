package com.dachen.community;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dachen.community.activity.BaseActivity;

/**
 * Created by pqixi on 2016/9/8 0008.
 */
public class Test extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //左边没有返回的样式，右边有一个按钮
        initView("社区",R.layout.activity_publish_layout,R.drawable.icon_gcoding);
        setLeftLayoutDisable();


        //左边有返回的样式，右边有两个或以上按钮
        //initView("社区",R.layout.activity_publish_layout,R.drawable.icon_gcoding,R.drawable.icon_gcoding);

        //左边有返回的样式，右边有两个或以上按钮加文字
        //initView("社区",R.layout.activity_publish_layout,R.drawable.icon_gcoding,R.string.name);
    }

    @Override
    protected void rightClick(int index) {
        super.rightClick(index);
        switch (index){
            case 0:
                //第一个按钮的点击事件
                break;
            case 1:
                //第二个按钮的点击事件
                break;
        }
    }
}
