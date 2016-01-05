package com.ddschool.activity;

//import imsdk.data.IMMyself;
//import imsdk.data.IMMyself.OnActionListener;
//import imsdk.data.IMMyself.OnAutoLoginListener;
//import imsdk.data.IMSDK;
//import imsdk.data.mainphoto.IMSDKMainPhoto;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ddschool.R;
import com.ddschool.bean.UserInfo;
import com.ddschool.bean.UserToken;
import com.ddschool.ui.LoadingDialog;
import com.ddschool.ui.TipsToast;
import com.ddschool.ui.UICommon;
import com.ddschool.utils.JPushUtil;
import com.frame.common.HttpUtil;
import com.frame.common.MD5Util;
import com.frame.common.ThreadPoolUtils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

//import com.imsdk.imdeveloper.app.IMApplication;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private SharedPreferences mySharedPreferences;
    private final int What_Login = 0x01;
    private final int What_Reg = 0x02;
    private EditText mUserNameEditText; // 帐号编辑框
    private EditText mPasswordEditText; // 密码编辑框

    private ImageView mImageView;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    private CheckBox mRememberMe;

    private LoadingDialog mDialog;

    private long mExitTime;

    private final static int SUCCESS = 0;
    private final static int FAILURE = -1;

    private static TipsToast mTipsToast;

    public static boolean isForeground = false;
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.ddschool.jpush.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使得音量键控制媒体声音
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //layout
        setContentView(R.layout.activity_login);
        //JPUSh
        registerMessageReceiver();  // used for receive msg
        initView();
        initListener();
    }


    private void initView() {

        mUserNameEditText = (EditText) findViewById(R.id.login_user_name_edittext);
        mPasswordEditText = (EditText) findViewById(R.id.login_password_edittext);
        mRememberMe = (CheckBox) findViewById(R.id.select_remember_me);

        mLoginBtn = (Button) findViewById(R.id.login_login_btn);
        mRegisterBtn = (Button) findViewById(R.id.login_register_btn);

        mImageView = (ImageView) findViewById(R.id.login_imageview);

        //设置默认用户名
        mUserNameEditText.addTextChangedListener(mTextWatcher);
        mySharedPreferences = getSharedPreferences("imsdk", Activity.MODE_PRIVATE);
        mUserNameEditText.setText(mySharedPreferences.getString("userName", ""));
        mPasswordEditText.setText(mySharedPreferences.getString("userpass", ""));
    }

    private void initListener() {

        mLoginBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson g = new Gson();
            Log.i("What", "what=" + msg.what + ",json=" + msg.obj);
            switch (msg.what) {
                case What_Login:
                    UserInfo info = (UserInfo) msg.obj;
                    if (info.getErrcode() == 0) {
                        Log.d("Token", info.toString());
                        updateStatus(SUCCESS);
                    } else {
                        updateStatus(FAILURE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void updateStatus(int status) {
        mLoginBtn.setEnabled(true);

        switch (status) {
            case SUCCESS: {
                mDialog.dismiss();
                UICommon.showTips(LoginActivity.this, R.mipmap.tips_smile, "登录成功");

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                //intent.putExtra("userName", mUserNameEditText.getText().toString());
                startActivity(intent);

                //缓存用户名
                if (mRememberMe.isChecked()) {
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putString("userName", mUserNameEditText.getText().toString());
                    editor.putString("userpass", mPasswordEditText.getText().toString());
                    editor.commit();
                }
                Log.i("等LoginActivity", "123");
                LoginActivity.this.finish();
            }
            break;
            case FAILURE:
                mDialog.dismiss();
                mLoginBtn.setEnabled(true);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login_btn:
                mDialog = new LoadingDialog(this, "正在登录...");
                mDialog.setCancelable(true);
                mDialog.show();
                login();
                break;
            case R.id.login_register_btn: {
//			Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//			startActivityForResult(intent, 1);
            }
            break;
            default:
                break;
        }
    }

    private class LoginRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Log.d("AccessToken", UserToken.getInstance(getApplicationContext()).getAccessToken());
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("phone", mUserNameEditText
                        .getText().toString()));
                list.add(new BasicNameValuePair("password", MD5Util
                        .getMD5String(mPasswordEditText.getText().toString())));
                Log.d("MD5", MD5Util.getMD5String(mPasswordEditText.getText().toString()));
                list.add(new BasicNameValuePair("access_token", UserToken.getInstance(getApplicationContext())
                        .getAccessToken()));
                Log.d("请求参数", list.toString());
                String json = HttpUtil.sendPostRequest("http://schoolapi2.wo-ish.com/user/login", list);
                Gson gson = new Gson();
                UserInfo info = gson.fromJson(json, UserInfo.class);

                Message msg = Message.obtain();
                msg.what = What_Login;
                msg.obj = info;
                handler.sendMessage(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void login() {
        ThreadPoolUtils.execute(new LoginRunnable());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {

                    String username = data.getStringExtra("username");
                    //缓存用户名
                    if (mRememberMe.isChecked()) {
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("userName", username);
                        editor.commit();
                    }

                    //注册成功，已是登录状态，
                    //跳转首页
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    intent.putExtra("userName", username);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }

                break;
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                UICommon.showTips(LoginActivity.this, R.mipmap.tips_smile, "再按一次返回桌面");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
        setAlias();
    }

    private void setAlias() {
        String alias = UserInfo.getInstance().getData().getUserid();
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(LoginActivity.this, "别名为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!JPushUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(LoginActivity.this, "标签为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final String TAG = "LoginActivity";
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (JPushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            JPushUtil.showToast(logs, getApplicationContext());
        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (JPushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            JPushUtil.showToast(logs, getApplicationContext());
        }

    };

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!JPushUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}