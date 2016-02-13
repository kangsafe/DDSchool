//package com.ddschool.activity;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.ddschool.R;
//
//public class Main2Activity extends AppCompatActivity {
//
//        private static final String APP_NAME = "Ped_android";
//        private static final String VERSION = "1.0.0";
//        private static final String INFO_NAME = "计步器";
//        private static final String STORE_APK = "chunyu_apk";
//
//        @Bind(R.id.main_b_install_apk)
//        Button mBInstallApk;
//
//        private UpdateAppUtils.UpdateCallback mUpdateCallback; // 更新回调
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//            ButterKnife.bind(this);
//
//            mUpdateCallback = new UpdateAppUtils.UpdateCallback() {
//                @Override public void onSuccess(UpdateInfo updateInfo) {
//                    Toast.makeText(Main2Activity.this, "有更新", Toast.LENGTH_SHORT).show();
//                    UpdateAppUtils.downloadApk(Main2Activity.this, updateInfo, INFO_NAME, STORE_APK);
//                }
//
//                @Override public void onError() {
//                    Toast.makeText(Main2Activity.this, "无更新", Toast.LENGTH_SHORT).show();
//                }
//            };
//
//            mBInstallApk.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    UpdateAppUtils.checkUpdate(APP_NAME, VERSION, mUpdateCallback);
//                }
//            });
//        }
//}
