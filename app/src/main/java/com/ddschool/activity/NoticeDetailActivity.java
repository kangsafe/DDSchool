package com.ddschool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.ddschool.R;
import com.ddschool.bean.NoticeDetail;
import com.ddschool.bean.UserToken;
import com.frame.common.HttpUtil;
import com.frame.common.ThreadPoolUtils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class NoticeDetailActivity extends BaseActivity {
    private static final String TAG = "NoticeDetailActivity";
    private Button btnBack;
    private TextView title;
    private WebView webview;
    private String nid;
    private final int What_Detail = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        Intent intent = getIntent();
        nid = intent.getStringExtra("nid");
        initUI();
    }

    private void initUI() {
        btnBack = (Button) findViewById(R.id.title_btn_left);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticeDetailActivity.this, NoticeActivity.class);
                startActivity(intent);
                NoticeDetailActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
            }
        });
        title = (TextView) findViewById(R.id.title_txt);
        webview = (WebView) findViewById(R.id.webview);
        ThreadPoolUtils.execute(new NoticeDetailRunnable());
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("What", "what=" + msg.what + ",json=" + msg.obj);
            switch (msg.what) {
                case What_Detail:
                    NoticeDetail info = (NoticeDetail) msg.obj;
                    if (info != null && info.getErrcode() == 0) {
                        Log.d(TAG, info.toString());
                        NoticeDetail.NoticeDetailData data = info.getData();
                        title.setText(data.getTitle().length() > 10 ? data.getTitle().substring(0, 10) + "..." : data.getTitle());
                        String body = getNewsDetails(data);
                        webview.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public String getNewsDetails(NoticeDetail.NoticeDetailData info) {

        String data = "<body>" +
                "<center><h2 style='font-size:16px;'>" + info.getTitle() + "</h2></center>"
                + "<p align='center' style='margin-left:10px'>"
                + "<span style='font-size:10px;'>"
                + info.getDepname() + "&nbsp;&nbsp;" + info.getTname() + "&nbsp;&nbsp;" + info.getDate()
                + "</span>"
                + "</p>"
                + "<hr size='1' />"
                + "<article>" + info.getContent() + "</article>"
                + "</body>";
        Log.i(TAG, data);
        return data;
    }

    private class NoticeDetailRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Log.d("AccessToken", UserToken.getInstance(getApplicationContext()).getAccessToken());
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("nid", nid));
                list.add(new BasicNameValuePair("access_token", UserToken.getInstance(getApplicationContext())
                        .getAccessToken()));
                Log.d("请求参数", list.toString());
                String json = HttpUtil.sendPostRequest("http://schoolapi2.wo-ish.com/notice/detail", list);
                Gson gson = new Gson();
                NoticeDetail info = gson.fromJson(json, NoticeDetail.class);

                Message msg = Message.obtain();
                msg.what = What_Detail;
                msg.obj = info;
                handler.sendMessage(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
