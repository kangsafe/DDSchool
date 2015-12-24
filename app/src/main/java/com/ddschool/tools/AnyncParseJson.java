package com.ddschool.tools;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * json异步解析类
 * @author 蒙查查
 * **/
public class AnyncParseJson<T> extends Thread {

    JSONObject jobj;
    Class<T> tclass;
    Handler mHandler;

    public AnyncParseJson(JSONObject jobj, Class<T> tclass, Handler mHandler) {
        // TODO Auto-generated constructor stub
        this.jobj = jobj;
        this.tclass = tclass;
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        Gson gson = new Gson();
        T tobj = gson.fromJson(jobj.toString(), tclass);
        Message msg = mHandler.obtainMessage();
        msg.obj = tobj;
        mHandler.sendMessage(msg);
    }
}