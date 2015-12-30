package com.ddschool.bean;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserToken {
    private static String AccessToken;
    private static int Timeout;
    private static UserToken instance;
    private static SharedPreferences mySharedPreferences;
    private static Context mContext;

    private UserToken() {
    }

    public static UserToken getInstance(Context context) {
        if (instance == null) {
            instance = new UserToken();
        }
        mContext = context;
        return instance;
    }

    public static String getAccessToken() {
        if (AccessToken.isEmpty()) {
            mySharedPreferences = mContext.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
            AccessToken = mySharedPreferences.getString("AccessToken", "");
            Timeout = mySharedPreferences.getInt("Timeout", 0);
        }
        return AccessToken;
    }

    public static void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public static int getTimeout() {
        return Timeout;
    }

    public static void setTimeout(int timeout) {
        Timeout = timeout;
    }
}
