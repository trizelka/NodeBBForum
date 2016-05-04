package com.trizelka.myforum.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.trizelka.myforum.ForumService;
import com.trizelka.myforum.R;
import com.trizelka.myforum.adapter.PublicAdapter;
import com.trizelka.myforum.model.Categories;
import com.trizelka.myforum.model.Topics;
import com.trizelka.myforum.utils.JSONSharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener  {
    private static final String TAG = "MainActivity";
    private static ExpandableListView CategoriesListView;
    private PublicAdapter publicAdapter;
    private SwipeRefreshLayout pullToRefresh;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initialize();
        DisplayCategories();
    }

    private void initialize () {
        CategoriesListView = (ExpandableListView) findViewById(R.id.categoriesListView);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(this);

        Intent intent = new Intent(this, ForumService.class);
        intent.setAction(ForumService.ACTION_GET_PUBLIC);
        this.startService(intent);
    }

    private void DisplayCategories() {
        ArrayList<Categories> categorieslist = new ArrayList<Categories>();
        try {
            JSONObject data = JSONSharedPreferences.loadJSONObject(getApplicationContext(), "DATA_PUBLIC", "_PUBLIC");
            JSONArray darray = data.optJSONArray("categories");
            for (int i=0; i<darray.length();i++) {
                ArrayList<Topics> topic_list;
                JSONObject dobject = darray.getJSONObject(i);

                Categories cate = new Categories();
                cate.setCid(dobject.getInt("cid"));
                cate.setCategories(Html.fromHtml(dobject.getString("name")).toString());
                cate.setDescription(dobject.getString("description"));
                cate.setTopicCount(dobject.getInt("topic_count"));
                cate.setPostCount(dobject.getInt("post_count"));
                cate.setIcon(dobject.getString("icon"));
                cate.setBgcolor(dobject.getString("bgColor"));
                cate.setSlug(dobject.getString("slug"));
                cate.setFontColor(dobject.getString("color"));

                try {
                    JSONObject teaser = dobject.getJSONObject("teaser");
                    cate.setTeaserSlug(teaser.getString("url"));
                    cate.setTeaserTimestamp_lastpost(teaser.getString("timestampISO"));
                }catch(Exception ee){}
                Log.w(TAG, "result cate: " + cate.getCid() + "," + cate.getCategories());
                topic_list = new ArrayList<Topics>();
                JSONArray dtopic = dobject.getJSONArray("posts");
                if (dtopic.length()==0) {
                 Log.w(TAG, "result posts: " + 0);
                }else {
                    for (int i2 = 0; i2 < dtopic.length(); i2++) {
                        try {
                            Topics dtp = new Topics();
                            JSONObject dsub = dtopic.getJSONObject(i2);

                            dtp.setCid(dsub.getInt("cid"));
                            JSONObject topicdtp = dsub.getJSONObject("topic");
                            dtp.setTopic(topicdtp.getString("title"));
                            dtp.setContent_post(Html.fromHtml(dsub.getString("content")).toString());
                            dtp.setTimestamp_lastpost(dsub.getString("timestamp"));

                            JSONObject usr = dsub.getJSONObject("user");
                            dtp.setUser_post(usr.getString("username"));
                            dtp.setIcon_text_post(usr.getString("icon:text"));
                            dtp.setIcon_bgcolor_post(usr.getString("icon:bgColor"));
                            Log.w(TAG, "dtp result: " + dtp.getCid() + ", " + dtp.getTopic() + ", " + dtp.getContent_post() + ", " + dtp.getTimestamp_lastpost() + ", " + dtp.getUser_post());
                            topic_list.add(dtp);
                            Log.w(TAG,"add dtp: "+topic_list.get(0).toString());
                        } catch (Exception ee) {
                            Log.w(TAG, "error dtp: " + ee);
                        }

                        cate.setTopics(topic_list);
                        Log.w(TAG, "set topics topic_list: "+cate.getTopics().get(0));
                    }

                }
                Log.w(TAG,"category last timestamp: "+ cate.getTeaserTimestamp_lastpost());
                categorieslist.add(cate);
                Log.w(TAG, "add categorieslist");

                publicAdapter = new PublicAdapter(MainActivity.this, categorieslist);
                Log.w(TAG,"add publicadapter");
                CategoriesListView.setAdapter(publicAdapter);
                Log.w(TAG, "CategoriesListView setadapter");
            }
        }catch(Exception ee){
            Log.w(TAG,"error diluar: "+ ee);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), ForumService.class);
                intent.setAction(ForumService.ACTION_GET_PUBLIC);
                getApplicationContext().startService(intent);
                DisplayCategories();
                pullToRefresh.setRefreshing(false);
            }
        }, 1000);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
