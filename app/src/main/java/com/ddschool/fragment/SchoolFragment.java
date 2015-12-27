package com.ddschool.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.ddschool.R;
import com.ddschool.activity.NoticeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolFragment extends Fragment {

    private static String[] titles = null;
    private SimpleAdapter gridAdapter;
    private GridView gridView;
    private ArrayList<HashMap<String, Object>> items;

    public SchoolFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_school, container, false);

        items = new ArrayList<HashMap<String, Object>>();
        this.gridView = (GridView) v.findViewById(R.id.item_grid);
        titles = getResources().getStringArray(R.array.school_items_titles);
        int[] iconResourse = {
                R.mipmap.school_sign,
                R.mipmap.school_notice,
                R.mipmap.school_smallpaper,
                R.mipmap.school_share,
                R.mipmap.tbsweb,
                R.mipmap.tbsvideo,
                R.mipmap.imageselect,
                R.mipmap.tbs_db,
                R.mipmap.firstx5,
                R.mipmap.webviewtransport,
                R.mipmap.sysweb,
                R.mipmap.flash,
                R.mipmap.webtips,
                R.mipmap.securityjs,
                R.mipmap.longclick,
        };

        HashMap<String, Object> item = null;
        //HashMap<String, ImageView> block = null;
        for (int i = 0; i < titles.length; i++) {
            item = new HashMap<String, Object>();
            item.put("title", titles[i]);
            item.put("icon", iconResourse[i]);
            item.put("id", i);
            items.add(item);
        }
        this.gridAdapter = new SimpleAdapter(v.getContext().getApplicationContext(), items, R.layout.content_school_item,
                new String[]{"title", "icon"},
                new int[]{R.id.Item_text, R.id.Item_bt});
        if (null != this.gridView) {
            this.gridView.setAdapter(gridAdapter);
            this.gridAdapter.notifyDataSetChanged();
            this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> gridView, View view, int position, long id) {
                    HashMap<String, String> item = (HashMap<String, String>) gridView.getItemAtPosition(position);

                    String current_title = item.get("title");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    switch (position) {
                        case 0://签到信息:
                            SchoolSignFragment schoolSignFragment = new SchoolSignFragment();
                            ft.replace(R.id.content_container, schoolSignFragment);
                            ft.commit();
                            break;
                        case 1://通知公告
//                            SchoolNoticeFragment schoolNoticeFragment = new SchoolNoticeFragment();
//                            ft.replace(R.id.content_container, schoolNoticeFragment);
//                            ft.commit();
                            Intent intent=new Intent().setClass(getContext(), NoticeActivity.class);
                            startActivity(intent);
                            break;
                        case 2://小纸条
                            ItemFragment itemFragment=new ItemFragment();
                            ft.replace(R.id.content_container, itemFragment);
                            ft.commit();
                            break;
                        default:
                            break;
                    }

                }
            });
        }
        return v;
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
        }
    }
}
