package com.ddschool.fragment;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.ddschool.R;
import com.ddschool.utils.HttpHelper;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SchoolSignFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SchoolSignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchoolSignFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private final String TAG = "SchoolSignFragment";

    public SchoolSignFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchoolSignFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SchoolSignFragment newInstance(String param1, String param2) {
        SchoolSignFragment fragment = new SchoolSignFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Map<String, String> map = new HashMap<>();
        String str = "";
        try {
            ApplicationInfo appInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(),
                    PackageManager.GET_META_DATA);
            Log.i("TAG", appInfo.metaData.getString("DDSchool_AppId"));
            map.put("grant_type", "client_credential");
            map.put("appid", appInfo.metaData.getString("DDSchool_AppId"));
            map.put("appsecret", appInfo.metaData.getString("DDSchool_AppSecret"));
            for (String key : map.keySet()
                    ) {
                str += key + "=" + map.get(key) + "&";
            }
            str = str.substring(0, str.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, str);
//        com.ddschool.net.HttpHelper.AysnPostHtml("http://schoolapi2.wo-ish.com", str, new HttpConnProp(), "utf-8", new com.ddschool.net.HttpHelper.AsynExecuteCallBack() {
//            @Override
//            public void beforeExecute() {
//
//            }
//
//            @Override
//            public void afterExecute(ReturnData rData) {
//                System.out.print(rData);
//            }
//
//            @Override
//            public void exceptionOccored(Exception e) {
//
//            }
//        });
//        com.ddschool.net.HttpHelper.shutTheThreadPoolDown();
        HttpHelper httpHelper = new HttpHelper();
        byte[] b = str.getBytes();
        try {
            str = new String(b, "UTF-8");//new String(str.getBytes(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpHelper.getHtmlByThread("http://schoolapi2.wo-ish.com/OAuth/token", str, true, "utf-8", handler, 0);
    }

    private Handler handler = new Handler() {
        // 子类必须重写此方法,处理消息
        @Override
        public void handleMessage(Message msg) {
            Log.d("MyHandler", "handleMessage......");
            Log.i("TAG", msg.obj.toString());
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    System.out.print(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_school_sign, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class SchoolAdapter extends SimpleAdapter {

        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         * @param from     A list of column names that will be added to the Map associated with each
         *                 item.
         * @param to       The views that should display column in the "from" parameter. These should all be
         *                 TextViews. The first N views in this list are given the values of the first N columns
         */
        public SchoolAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            HttpHelper httpHelper = new HttpHelper();
            String html = httpHelper.getHtml("", "", true, "utf-8");

        }
    }
}
