package com.ddschool.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddschool.R;
import com.ddschool.activity.CityListActivity;
import com.ddschool.activity.DetailsActivity;
import com.ddschool.adapter.NewsAdapter;
import com.ddschool.bean.NoticeList;
import com.ddschool.bean.UserToken;
import com.ddschool.lib.view.HeadListView;
import com.ddschool.tools.Constants;
import com.ddschool.widget.XListView;
import com.frame.common.HttpUtil;
import com.frame.common.ThreadPoolUtils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsFragment extends Fragment implements XListView.IXListViewListener {
    private final static String TAG = "NewsFragment";
    Activity activity;
    ArrayList<NoticeList.NoticeListItem> newsList = new ArrayList<NoticeList.NoticeListItem>();
    //HeadListView mListView;
    private XListView mListView;
    NewsAdapter mAdapter;
    String text;
    int channel_id;
    ImageView detail_loading;
    public final static int SET_NEWSLIST = 0;
    public final static int What_NoticList = 0x01;
    //Toast提示框
    private RelativeLayout notify_view;
    private TextView notify_view_text;
    private int pIndex = 1;
    private int RefreshNums = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Bundle args = getArguments();
        text = args != null ? args.getString("text") : "";
        channel_id = args != null ? args.getInt("id", 0) : 0;
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = activity;
        super.onAttach(activity);
    }

    /**
     * 此方法意思为fragment是否可见 ,可见时候加载数据
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //fragment可见时加载数据
            if (newsList != null && newsList.size() != 0) {
                handler.obtainMessage(What_NoticList).sendToTarget();
            } else {
                ThreadPoolUtils.execute(new NoticeRunnable());
            }
        } else {
            //fragment不可见时不执行操作
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
        //mListView = (HeadListView) view.findViewById(R.id.mListView);
        mListView = (XListView) view.findViewById(R.id.mListView);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(getTime());
        mAdapter = new NewsAdapter(activity, newsList);
        mListView.setAdapter(mAdapter);

        TextView item_textview = (TextView) view.findViewById(R.id.item_textview);
        detail_loading = (ImageView) view.findViewById(R.id.detail_loading);
        //Toast提示框
        notify_view = (RelativeLayout) view.findViewById(R.id.notify_view);
        notify_view_text = (TextView) view.findViewById(R.id.notify_view_text);
        item_textview.setText(text);

        return view;
    }

    /*
        * 下拉刷新
        * */
    @Override
    public void onRefresh() {
        mAdapter = null;
        pIndex = 1;
        ThreadPoolUtils.execute(new NoticeRunnable());
    }

    /*
    * 加载更多
    * */
    @Override
    public void onLoadMore() {
        ThreadPoolUtils.execute(new NoticeRunnable());
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(getTime());
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    private void initData() {
        //newsList = Constants.getNewsList();
//        ThreadPoolUtils.execute(new NoticeRunnable());

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case What_NoticList:
                    NoticeList noticeList = new Gson().fromJson(msg.obj.toString(), NoticeList.class);
                    if (noticeList.getErrcode() == 0) {
                        pIndex++;
                        RefreshNums = noticeList.getData().size();
                        //刷新
                        if (mAdapter == null) {
                            newsList.clear();
                            newsList = noticeList.getData();
                            mAdapter = new NewsAdapter(activity, newsList);
                            mListView.setAdapter(mAdapter);

                        } else {//添加
                            newsList.addAll(noticeList.getData());
                            mAdapter.notifyDataSetChanged();
                        }
                        onLoad();
                    } else {
                        RefreshNums = 0;
                    }
                    if (RefreshNums < 1) {
                        Log.i(TAG, "RefreshNums");
                        Toast.makeText(activity, "已到最后一条数据", Toast.LENGTH_SHORT);
                    }
                    detail_loading.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /* 初始化选择城市的header*/
    public void initCityChannel() {
        View headview = LayoutInflater.from(activity).inflate(R.layout.city_category_list_tip, null);
        TextView chose_city_tip = (TextView) headview.findViewById(R.id.chose_city_tip);
        chose_city_tip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(activity, CityListActivity.class);
                startActivity(intent);
            }
        });
        mListView.addHeaderView(headview);
    }

    /* 初始化通知栏目*/
    private void initNotify() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                notify_view_text.setText(String.format(getString(R.string.ss_pattern_update), RefreshNums));
                notify_view.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        notify_view.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        }, 1000);
    }

    /* 摧毁视图 */
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        Log.d("onDestroyView", "channel_id = " + channel_id);
        mAdapter = null;
    }

    /* 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "channel_id = " + channel_id);
    }

    private class NoticeRunnable implements Runnable {
        @Override
        public void run() {
            try {
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("type", String.valueOf(channel_id + 1)));
                list.add(new BasicNameValuePair("pageindex", String.valueOf(pIndex)));
                list.add(new BasicNameValuePair("access_token",
                        UserToken.getInstance().getAccessToken()));
                Log.i("请求参数", list.toString());
                String json = HttpUtil.sendPostRequest("http://schoolapi2.wo-ish.com/notice/list", list);
                Log.i(TAG, json);
                Message msg = Message.obtain();
                msg.what = What_NoticList;
                msg.obj = json;
                handler.sendMessage(msg);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
