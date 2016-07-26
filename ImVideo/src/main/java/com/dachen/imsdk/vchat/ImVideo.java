package com.dachen.imsdk.vchat;

import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.ImSdk.OnImEnvListener;
import com.dachen.imsdk.vchat.control.QavsdkControl;

/**
 * Created by Mcp on 2016/7/21.
 */
public class ImVideo {
    private static ImVideo mInstance;
    public QavsdkControl mQavsdkControl;
    private OnImEnvListener envListener;

    public static synchronized ImVideo getInstance(){

        if(mInstance==null){
            mInstance=new ImVideo();
        }
        return mInstance;
    }
    public void injectListener(){
        envListener=new OnImEnvListener() {
            @Override
            public void onInitContext() {
                mQavsdkControl=new QavsdkControl(ImSdk.getInstance().context);
            }

            @Override
            public void onChangeIp(String baseIp) {
                VChatUtil.changeIp(baseIp);
            }
        };
        ImSdk.getInstance().mEnvListeners.add(envListener);
    }
}
