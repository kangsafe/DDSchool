package com.ddschool.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ddschool.bean.BwConst;
import com.ddschool.bean.BwToken;
import com.ddschool.bean.UserInfo;
import com.ddschool.bean.UserToken;
import com.ddschool.service.TokenRunnable;
import com.ddschool.utils.PrefsConsts;
import com.ddschool.utils.SpUtil;
import com.frame.common.HttpUtil;
import com.frame.common.ThreadPoolUtils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.InstrumentedActivity;

public class BaseSuperActivity extends InstrumentedActivity {
    protected static String access_token;
    protected static UserInfo.UserInfoData userInfo;
    protected SharedPreferences mySharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySharedPreferences=SpUtil.getSharePerference(PrefsConsts.PREFS_DDSCHOOL_TOKEN_ACCESSTOKEN,getApplicationContext());
        //获取access_token
        if (access_token==null || access_token.isEmpty()) {
            access_token = UserToken.getInstance(getApplicationContext()).getToken();
            if(access_token==null || access_token.isEmpty()){
                ThreadPoolUtils.execute(new TokenRunnable(getApplicationContext(),handler));
            }
        }
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson g = new Gson();
            Log.i("What", "what=" + msg.what + ",json=" + msg.obj);
            switch (msg.what) {
                case BwConst.What_Token:
                    BwToken jb = (BwToken)msg.obj;
                    if (jb.getErrcode() == 0) {
                        access_token=jb.getData().getAccess_token();
                        Log.i("Token", jb.getData().getAccess_token());
                        UserToken.getInstance(getApplicationContext()).setAccessToken(jb.getData().getAccess_token());
                        UserToken.getInstance(getApplicationContext()).setTimeout(jb.getData().getExpires_in() - 20);

                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("AccsessToken", jb.getData().getAccess_token());
                        editor.putInt("Timeout", jb.getData().getExpires_in() - 20);
                        editor.commit();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}