package com.ddschool.app;

import android.app.Application;
import android.app.Notification;
import android.util.Log;

import com.ddschool.R;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * For developer startup JPush SDK
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class BaseApplication extends Application {
    private static final String TAG = "JPush";

    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "[BaseApplication] onCreate");
         super.onCreate();
         
         JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush

        BasicPushNotificationBuilder builder1 = new BasicPushNotificationBuilder(this);
        builder1.statusBarDrawable = R.mipmap.arrow_up;
        //设置为自动消失
        builder1.notificationFlags = Notification.FLAG_AUTO_CANCEL;
        // 设置为铃声与震动都要
        builder1.notificationDefaults = Notification.DEFAULT_SOUND;// ｜ Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
        JPushInterface.setPushNotificationBuilder(1, builder1);
        // 指定定制的 Notification Layout
        CustomPushNotificationBuilder builder2 = new CustomPushNotificationBuilder(this,
                R.layout.activity_login, R.id.icon, R.id.title, R.id.text);
        builder2.statusBarDrawable = R.mipmap.ic_launcher;      // 指定最顶层状态栏小图标
        builder2.layoutIconDrawable = R.mipmap.ic_launcher1;   // 指定下拉状态栏时显示的通知图标
        builder2.notificationFlags=Notification.FLAG_ONLY_ALERT_ONCE;
        builder2.notificationDefaults=Notification.DEFAULT_SOUND;
        JPushInterface.setPushNotificationBuilder(3, builder2);
    }
}
