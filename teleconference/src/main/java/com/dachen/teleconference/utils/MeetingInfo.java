package com.dachen.teleconference.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by TianWei on 2016/9/5.
 */
public class MeetingInfo {
    private static final String KEY_MEETING_STATUS = "meeting_status";
    private static final String KEY_MEETING_ROLE = "meeting_role";
    private static final String KEY_MEETING_CHANNEL = "meeting_channel";
    private static final String KEY_MEETING_TOKEN = "meeting_token";
    private final Context mContext;
    private static final String SP_NAME = "teleconference_status_info";// FILE_NAME
    private static MeetingInfo INSTANCE;
    private SharedPreferences sp;

    private MeetingInfo(Context context) {
        mContext = context;
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
    }

    public static MeetingInfo getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MeetingInfo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MeetingInfo(context);
                }
            }
        }
        return INSTANCE;
    }

    public void setMeetingStatus(int status) {
        sp.edit().putInt(KEY_MEETING_STATUS, status).commit();
    }

    public int getMeetingStatus() {
        return sp.getInt(KEY_MEETING_STATUS, 0);
    }

    public void setMeetingRole(int role) {
        sp.edit().putInt(KEY_MEETING_ROLE, role).commit();
    }

    public int getMeetingRole() {
        return sp.getInt(KEY_MEETING_ROLE, 0);
    }

    public void setMeetingChannel(String channel) {
        sp.edit().putString(KEY_MEETING_CHANNEL, channel).commit();
    }

    public String getMeetingChannel() {
        return sp.getString(KEY_MEETING_CHANNEL, "");
    }

    public void setAgroaToken(String token) {
        sp.edit().putString(KEY_MEETING_TOKEN, token).commit();
    }

    public String getAgroToken() {
        return sp.getString(KEY_MEETING_TOKEN, "");
    }

}
