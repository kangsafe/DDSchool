package com.ddschool.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ddschool.bean.BwConst;
import com.ddschool.bean.BwToken;
import com.ddschool.bean.UserToken;
import com.frame.common.HttpUtil;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 */
public class TokenRunnable implements Runnable {
    private Context mContxt;
    private SharedPreferences mySharedPreferences;
    private Handler mHandler;
    public TokenRunnable(Context context,Handler handler){
        mContxt=context;
        mHandler=handler;
        mySharedPreferences=mContxt.getSharedPreferences("DDSchool", Activity.MODE_PRIVATE);
    }
    @Override
    public void run() {
        try {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("appid", BwConst.AppId));
            list.add(new BasicNameValuePair("appsecret", BwConst.AppSecret));
            list.add(new BasicNameValuePair("grant_type",
                    "client_credential"));
            Log.i("请求参数", list.toString());
            String json = HttpUtil.sendPostRequest("http://schoolapi2.wo-ish.com/oauth/token", list);
            Gson g=new Gson();
            BwToken jb = g.fromJson(json,
                    BwToken.class);
            if (jb.getErrcode() == 0) {
                Log.i("Token", jb.getData().getAccess_token());
                UserToken.getInstance(mContxt).setAccessToken(jb.getData().getAccess_token());
                UserToken.getInstance(mContxt).setTimeout(jb.getData().getExpires_in() - 20);

                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("AccsessToken", jb.getData().getAccess_token());
                editor.putInt("Timeout", jb.getData().getExpires_in() - 20);
                editor.commit();
            }
            if(mHandler!=null) {
                Message msg = Message.obtain();
                msg.what = BwConst.What_Token;
                msg.obj = jb;
                mHandler.sendMessage(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}