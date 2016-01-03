package com.ddschool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ddschool.R;
import com.ddschool.fragment.NoticeFragment;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Button btnBack;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        btnBack=(Button)findViewById(R.id.notice_btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(NoticeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnAdd=(Button)findViewById(R.id.notice_btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(NoticeActivity.this,NoticeAddActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        for (int i = 0; i < 2; i++) {
            NoticeFragment fragment = new NoticeFragment();
            Bundle args = new Bundle();
            args.putInt("id", i + 1);
            args.putString("text", "班级通知");
            fragment.setArguments(args);
            fragmentList.add(fragment);
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragmentList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_notice, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        FragmentManager fragmentManager;
        ArrayList<Fragment> list;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            fragmentManager = fm;
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return ItemFragment.newInstance(position + 1);
            //return PlaceholderFragment.newInstance(position + 1);
//            fragmentManager = getSupportFragmentManager();
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//            Fragment currentFragment = fragmentManager.findFragmentByTag("News" + position);
//            if (currentFragment == null) {
            //NoticeFragment fragment = new NoticeFragment();
            //Bundle args = new Bundle();
            //args.putInt("id", position + 1);
            //args.putString("text", "班级通知");
            //fragment.setArguments(args);
            //return fragment;
//                currentFragment = fragment;
//                ft.add(R.id.container, currentFragment, "News" + position);
//            }
////            if (lastFragment != null) {
////                ft.hide(lastFragment);
////            }
//            if (currentFragment.isDetached()) {
//                ft.attach(currentFragment);
//            }
//            ft.show(currentFragment);
//            //lastFragment = currentFragment;
//            ft.commit();
////            NoticeFragment fragment = new NoticeFragment();
////            Bundle args = new Bundle();
////            args.putInt("id", position);
////            args.putString("text","班级通知");
////            fragment.setArguments(args);
//            return currentFragment;
            return list.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "班级通知";
                case 1:
                    return "学校通知";
            }
            return null;
        }
    }
}
