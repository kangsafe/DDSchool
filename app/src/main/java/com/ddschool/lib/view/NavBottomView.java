package com.ddschool.lib.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ddschool.R;
import com.ddschool.activity.QQconstactActivity;

/**
 * 底部导航栏
 */

public class NavBottomView extends FrameLayout {

    public ImageButton btnSchool;
    public ImageButton btnContact;
    public ImageButton btnForum;
    public ImageButton btnSetting;

    private TextView app_cancle;
    private TextView app_exit;
    private TextView app_change;

    private View mPopView;
    private View currentButton;

    private PopupWindow mPopupWindow;
    private LinearLayout buttomBarGroup;


    private Context context;

    public NavBottomView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public NavBottomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public NavBottomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initUI();
    }

    /**
     * 初始化Views等UI
     */
    private void initUI() {
        //初始化组合组件布局
        LayoutInflater.from(context).inflate(R.layout.nav_bottom_view, this, true);
        buttomBarGroup = (LinearLayout) findViewById(R.id.bottom_bar_group);
        btnSchool = (ImageButton) buttomBarGroup.findViewById(R.id.bottom_school);
        btnContact = (ImageButton) buttomBarGroup.findViewById(R.id.bottom_constact);
        btnForum = (ImageButton) buttomBarGroup.findViewById(R.id.bottom_forum);
        btnSetting = (ImageButton) buttomBarGroup.findViewById(R.id.bottom_setting);
        btnSchool.setOnClickListener(newsOnClickListener);
        btnContact.setOnClickListener(constactOnClickListener);
        btnForum.setOnClickListener(deynaimicOnClickListener);
        btnSetting.setOnClickListener(settingOnClickListener);

        mPopView = LayoutInflater.from(context).inflate(R.layout.app_exit, null);
        mPopupWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

        app_cancle = (TextView) mPopView.findViewById(R.id.app_cancle);
        app_change = (TextView) mPopView.findViewById(R.id.app_change_user);
        app_exit = (TextView) mPopView.findViewById(R.id.app_exit);

        app_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        app_change.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(mContext, LoginActivity.class);
//                startActivity(intent);
//                ((Activity)mContext).overridePendingTransition(R.anim.activity_up, R.anim.fade_out);
//                finish();
            }
        });

        app_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
        currentButton = btnSchool;
        setButton(btnSchool);
    }

    public OnClickListener newsOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
//            FragmentManager fm=getSupportFragmentManager();
//            FragmentTransaction ft=fm.beginTransaction();
//            NewsFatherFragment newsFatherFragment=new NewsFatherFragment();
//            ft.replace(R.id.fl_content, newsFatherFragment,MainActivity.TAG);
//            ft.commit();
            setButton(v);
        }
    };

    public OnClickListener constactOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
//            FragmentManager fm=((Activity)v.getContext()).getSupportFragmentManager();
//            FragmentTransaction ft=fm.beginTransaction();
//            ConstactFatherFragment constactFatherFragment=new ConstactFatherFragment();
//            ft.replace(R.id.content_container, constactFatherFragment);
//            //ft.replace(R.id.content_container, constactFatherFragment,MainActivity.TAG);
//            ft.commit();
            setButton(v);
        }
    };

    public OnClickListener deynaimicOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
//            FragmentManager fm=getSupportFragmentManager();
//            FragmentTransaction ft=fm.beginTransaction();
//            DynamicFragment dynamicFragment=new DynamicFragment();
//            ft.replace(R.id.fl_content, dynamicFragment,MainActivity.TAG);
//            ft.commit();
            setButton(v);
        }
    };

    public OnClickListener settingOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
//            FragmentManager fm=getSupportFragmentManager();
//            FragmentTransaction ft=fm.beginTransaction();
//            SettingFragment settingFragment=new SettingFragment();
//            ft.replace(R.id.fl_content, settingFragment,MainActivity.TAG);
//            ft.commit();
            setButton(v);
        }
    };

    private void setButton(View v) {
        if (currentButton != null && currentButton.getId() != v.getId()) {
            currentButton.setEnabled(true);
        }
        v.setEnabled(false);
        currentButton = v;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //if (keyCode == KeyEvent.KEYCODE_MENU) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0000000")));
            mPopupWindow.showAtLocation(buttomBarGroup, Gravity.BOTTOM, 0, 0);
            mPopupWindow.setAnimationStyle(R.style.app_pop);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
            mPopupWindow.update();
        }
        return super.onKeyDown(keyCode, event);
    }
}