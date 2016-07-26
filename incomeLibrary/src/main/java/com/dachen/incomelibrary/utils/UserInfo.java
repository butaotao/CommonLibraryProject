package com.dachen.incomelibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfo {
    private static final String SP_NAME = "login_user_info_bank";// FILE_NAME
    private static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_NAME = "user_name";
    public static final String KEY_USERTYPE = "user_type";
    public static final String KEY_NET = "key_net";
    public static final String VERSION = "VERSION";
    public String getVersion() {
        return sp.getString(VERSION, "");
    }
    public void setVersion(String version){
        sp.edit().putString(VERSION, version).commit();
    }

    SharedPreferences sp;
    static UserInfo info;

    public UserInfo(Context c) {
        if (null == sp) {
            sp = c.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }

    }

    public static UserInfo getInstance(Context c) {
        if (null == info) {
            info = new UserInfo(c);
        }
        return info;

    }

    public String getId() {
        return sp.getString(KEY_USER_ID, "");
    }

    public String getUserType() {
        return sp.getString(KEY_USERTYPE, "");
    }

    public String getUserName() {
        return sp.getString(KEY_NAME, "");
    }

    public String getToken() {
        return sp.getString(KEY_ACCESS_TOKEN, "");
    }

    public String getKeyNet() {
        return sp.getString(KEY_NET, "");
    }

    public void setKeyNet(String keyNet) {
        sp.edit().putString(KEY_NET, keyNet).commit();

    }

    public void setId(String id) {
        sp.edit().putString(KEY_USER_ID, id).commit();
    }

    public void setUserType(String userType) {
        sp.edit().putString(KEY_USERTYPE, userType).commit();
    }

    public void setUserName(String userName) {
        sp.edit().putString(KEY_NAME, userName).commit();
    }

    public void setToken(String access_token) {
        sp.edit().putString(KEY_ACCESS_TOKEN, access_token).commit();
    }

}
