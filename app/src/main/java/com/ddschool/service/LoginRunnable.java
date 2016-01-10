package com.ddschool.service;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ddschool.bean.BwConst;
import com.ddschool.bean.UserInfo;
import com.ddschool.bean.UserToken;
import com.frame.common.HttpUtil;
import com.frame.common.MD5Util;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LoginRunnable implements Runnable {
    private Context mContxt;
    private SharedPreferences mySharedPreferences;
    private Handler mHandler;
    private String username;
    private String pass;
    public LoginRunnable(Context context,Handler handler,String username,String pass){
        mContxt=context;
        mHandler=handler;
        this.username=username;
        this.pass=pass;
        mySharedPreferences=mContxt.getSharedPreferences("DDSchool", Activity.MODE_PRIVATE);
    }
    @Override
    public void run() {
        try {
            Log.d("AccessToken", UserToken.getInstance(mContxt).getAccessToken());
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("phone", username));
            list.add(new BasicNameValuePair("password", MD5Util
                    .getMD5String(pass)));
            Log.d("MD5", MD5Util.getMD5String(pass));
            list.add(new BasicNameValuePair("access_token", UserToken.getInstance(mContxt)
                    .getAccessToken()));
            Log.d("请求参数", list.toString());
            String json = HttpUtil.sendPostRequest("http://schoolapi2.wo-ish.com/user/login", list);
            Gson gson = new Gson();
            UserInfo info = gson.fromJson(json, UserInfo.class);
            if (info.getErrcode() == 0) {
                Log.i("UserInfo", json);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("UserInfo", gson.toJson(info.getData()));
                editor.commit();
            }
            if(mHandler!=null) {
                Message msg = Message.obtain();
                msg.what = BwConst.What_Login;
                msg.obj = info;
                mHandler.sendMessage(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}