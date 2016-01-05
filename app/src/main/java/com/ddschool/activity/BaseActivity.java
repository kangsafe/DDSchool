package com.ddschool.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

public class BaseActivity extends Activity {

    private static final String TAG="BaseActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //JPushInterface.reportNotificationOpened(getApplicationContext(), savedInstanceState.getString(JPushInterface.EXTRA_MSG_ID));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"OnResume");
        //JPush统计
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"OnPause");
        //JPush统计
        JPushInterface.onPause(this);
    }
}
