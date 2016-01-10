package com.ddschool.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;

import com.ddschool.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {
    private long mSplashDelay = 2000;

    //private SharedPreferences mySharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使得音量键控制媒体声音
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        //mySharedPreferences = getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
        //ThreadPoolUtils.execute(new TokenRunnable());
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent().setClass(WelcomeActivity.this,
                        LoginActivity.class);

                startActivity(mainIntent);

                finish();

                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        };

        Timer timer = new Timer();

        timer.schedule(task, mSplashDelay);
//        Intent mainIntent = new Intent().setClass(WelcomeActivity.this,
//                LoginActivity.class);
//
//        startActivity(mainIntent);
//
//        finish();
//
//        overridePendingTransition(android.R.anim.fade_in,
//                android.R.anim.fade_out);

    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Gson g = new Gson();
//            Log.i("What", "what=" + msg.what + ",json=" + msg.obj);
//            switch (msg.what) {
//                case What_Token:
//                    BwToken jb = g.fromJson(msg.obj.toString(),
//                            BwToken.class);
//                    if (jb.getErrcode() == 0) {
//                        Log.i("Token", jb.getData().getAccess_token());
//                        UserToken.getInstance(getApplicationContext()).setAccessToken(jb.getData().getAccess_token());
//                        UserToken.getInstance(getApplicationContext()).setTimeout(jb.getData().getExpires_in() - 20);
//
//                        SharedPreferences.Editor editor = mySharedPreferences.edit();
//                        editor.putString("AccsessToken", jb.getData().getAccess_token());
//                        editor.putInt("Timeout", jb.getData().getExpires_in() - 20);
//                        editor.commit();
//                    } else {
//                        Toast.makeText(getApplicationContext(), jb.getErrmsg(),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    Intent mainIntent = new Intent().setClass(WelcomeActivity.this,
//                            LoginActivity.class);
//
//                    startActivity(mainIntent);
//
//                    finish();
//
//                    overridePendingTransition(android.R.anim.fade_in,
//                            android.R.anim.fade_out);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

//    private class TokenRunnable implements Runnable {
//        @Override
//        public void run() {
//            try {
//                List<NameValuePair> list = new ArrayList<>();
//                list.add(new BasicNameValuePair("appid", BwConst.AppId));
//                list.add(new BasicNameValuePair("appsecret", BwConst.AppSecret));
//                list.add(new BasicNameValuePair("grant_type",
//                        "client_credential"));
//                Log.i("请求参数", list.toString());
//                String json = HttpUtil.sendPostRequest("http://schoolapi2.wo-ish.com/oauth/token", list);
//                Message msg = Message.obtain();
//                msg.what = What_Token;
//                msg.obj = json;
//                handler.sendMessage(msg);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
}