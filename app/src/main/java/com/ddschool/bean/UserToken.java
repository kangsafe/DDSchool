package com.ddschool.bean;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.ddschool.utils.ApiServiceUtils;
import com.ddschool.utils.PrefsConsts;
import com.ddschool.utils.SpUtil;

import java.util.Date;

public class UserToken {
    private static String AccessToken;
    private static long Timeout;
    private static UserToken instance;
    private static SharedPreferences mySharedPreferences;
    private static Context mContext;

    private static ApiServiceUtils.TokenCallback tokenCallback = new ApiServiceUtils.TokenCallback() {
        @Override
        public void onSuccess(BwToken bwToken) {
            //UpdateAppUtils.downloadApk(UpdateAppActivity.this, updateInfo, INFO_NAME, STORE_APK);
            if (bwToken.getErrcode() == 0) {
                AccessToken=bwToken.getData().getAccess_token();
                Timeout=bwToken.getData().getExpires_in() - 20 + new Date().getTime();
                SpUtil.setStringSharedPerference(mySharedPreferences,
                        PrefsConsts.PREFS_DDSCHOOL_TOKEN_ACCESSTOKEN,
                        AccessToken
                );
                SpUtil.setLongSharedPerference(mySharedPreferences,
                        PrefsConsts.PREFS_DDSCHOOL_TOKEN_TIMEOUT,
                        Timeout
                );
            }
        }

        @Override
        public void onError() {
            //Toast.makeText(UpdateAppActivity.this, "无更新", Toast.LENGTH_SHORT).show();
        }
    };

    private UserToken() {
    }

    public static UserToken getInstance(Context context) {
        if (instance == null) {
            instance = new UserToken();
        }
        mContext = context;
        mySharedPreferences = SpUtil.getSharePerference(PrefsConsts.PREFS_DDSCHOOL_TOKEN_ACCESSTOKEN, mContext);
        return instance;
    }

    public static String getAccessToken() {
        if (AccessToken == null || AccessToken.isEmpty()) {
            mySharedPreferences = mContext.getSharedPreferences("DDSchool", Activity.MODE_PRIVATE);
            AccessToken = mySharedPreferences.getString("AccessToken", "");
            Timeout = mySharedPreferences.getInt("Timeout", 0);
        }
        return AccessToken;
    }

    public static String getToken() {
        if (AccessToken == null || AccessToken.isEmpty()) {
            AccessToken = mySharedPreferences.getString(PrefsConsts.PREFS_DDSCHOOL_TOKEN_ACCESSTOKEN, "");
            Timeout = mySharedPreferences.getInt(PrefsConsts.PREFS_DDSCHOOL_TOKEN_TIMEOUT, 0);
            if (AccessToken.isEmpty() || Timeout <= new Date().getTime()) {
                ApiServiceUtils.getToken(tokenCallback);
            }
        }
        return AccessToken;
    }

    public static void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public static long getTimeout() {
        return Timeout;
    }

    public static void setTimeout(long timeout) {
        Timeout = timeout;
    }
}
