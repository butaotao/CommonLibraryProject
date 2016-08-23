package com.dachen.teleconference;

import android.content.Context;



import io.agora.rtc.RtcEngine;

/**
 * @author gzhuo
 * @date 2016/8/16
 */
public class AgoraManager {
    private Context mContext;
    private static AgoraManager mInstance;
    private static RtcEngine mRtcEngine;
    private RtcEngineEventHandlerMgr mRtcEngineEventHandlerMgr;

    private AgoraManager() {
    }

    private AgoraManager(Context context) {
        mContext = context.getApplicationContext();
        mRtcEngineEventHandlerMgr = new RtcEngineEventHandlerMgr(mContext);
    }

    public static AgoraManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AgoraManager.class){
                mInstance = new AgoraManager(context);
            }
        }
        return mInstance;
    }

    public RtcEngine createRtcEngine(String vendorKey) {
        if (mRtcEngine == null) {
            mRtcEngine = RtcEngine.create(mContext, vendorKey, mRtcEngineEventHandlerMgr);
            mRtcEngine.monitorHeadsetEvent(true);
            mRtcEngine.monitorConnectionEvent(true);
            mRtcEngine.monitorBluetoothHeadsetEvent(true);
            mRtcEngine.enableHighPerfWifiMode(true);
        }
        return mRtcEngine;
    }

    public RtcEngine getRtcEngine() {
        return mRtcEngine;
    }

    public RtcEngineEventHandlerMgr getEventHandlerMgr(){
        return mRtcEngineEventHandlerMgr;
    }

}
