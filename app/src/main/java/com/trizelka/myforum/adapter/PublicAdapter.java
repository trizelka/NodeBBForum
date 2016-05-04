package com.trizelka.myforum.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.trizelka.myforum.model.Categories;
import com.trizelka.myforum.model.Topics;
import com.trizelka.myforum.R;

public class PublicAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "PublicAdapter";
    private Context context;
    private ArrayList<Categories> categories;

    public PublicAdapter(Context context, ArrayList<Categories> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Topics> chList = categories.get(groupPosition).getTopics();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        String sdatetime;
        Topics child = (Topics) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.topics_item, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.topic_name);
        TextView tv1 = (TextView) convertView.findViewById(R.id.content_post);
        TextView tv2 = (TextView) convertView.findViewById(R.id.timestamp_post);
        TextView tv3 = (TextView) convertView.findViewById(R.id.user_post);
        TextView tv4 = (TextView) convertView.findViewById(R.id.initial);

        tv.setText(child.getTopic().toString());
        tv1.setText(child.getContent_post().toString());
        try {
            sdatetime = child.getTimestamp_lastpost().substring(0, 10) + " " + child.getTimestamp_lastpost().substring(11, 19);
        }catch(Exception ee){
            sdatetime = "";
        }
        tv2.setText(sdatetime);
        tv3.setText(child.getUser_post().toString());
        tv4.setText(child.getIcon_text_post().toString());
        tv4.setBackgroundResource(R.drawable.circle_custome);
        GradientDrawable drawable = (GradientDrawable) tv4.getBackground();
        drawable.setColor(Color.parseColor(child.getIcon_bgcolor_post().toString()));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Topics> chList = categories.get(groupPosition).getTopics();;
        try {
            int chl = chList.size();
            return chl;
        }catch(Exception ee) {
            Log.w(TAG,"error: "+ee);
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Categories group = (Categories) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.categories_item, null);
        }

        Typeface font = Typeface.createFromAsset(context.getAssets(), "android-iconify-fontawesome.ttf");
        TextView gtv = (TextView) convertView.findViewById(R.id.categories_logo);
        TextView gtv1 = (TextView) convertView.findViewById(R.id.categories_name);
        TextView gtv2 = (TextView) convertView.findViewById(R.id.description);
        TextView gtv3 = (TextView) convertView.findViewById(R.id.ctopic);
        TextView gtv4 = (TextView) convertView.findViewById(R.id.cpost);
        TextView gtv5 = (TextView) convertView.findViewById(R.id.aboutdays);

        gtv.setTypeface(font);

        //if (group.getCid()==1) {
        //    gtv.setBackgroundResource(R.drawable.circle_orange);
        //    gtv.setText(R.string.fa_bullhorn);
        //}
        //if (group.getCid()==2) {
        //    gtv.setBackgroundResource(R.drawable.circle_blue);
        //    gtv.setText(R.string.fa_comments);
        //}
        //if (group.getCid()==4) {
        //    gtv.setBackgroundResource(R.drawable.circle_red);
        //    gtv.setText(R.string.fa_question);
        //}
        //if (group.getCid()==3) {
        //    gtv.setBackgroundResource(R.drawable.circle_green);
        //    gtv.setText(R.string.fa_newspaper_o);
        //}
        gtv.setBackgroundResource(R.drawable.circle_custome);
        GradientDrawable drawable = (GradientDrawable) gtv.getBackground();
        drawable.setColor(Color.parseColor(group.getBgcolor().toString()));
        gtv.setText(getStringResourceByName(group.getIcon().replace("-", "_")));

        Calendar c = Calendar.getInstance();

        long cmseconds = c.getTimeInMillis();
        long millis;
        try {
            millis = getDateInMillis(group.getTeaserTimestamp_lastpost().substring(0, 10) + " " + group.getTeaserTimestamp_lastpost().substring(11, 19));
            Log.w(TAG, "result: " + millis + ", " + cmseconds +", "+group.getTeaserTimestamp_lastpost().substring(0, 10) + " " + group.getTeaserTimestamp_lastpost().substring(11, 19));
        }catch(Exception ee) {
            millis=cmseconds;
        }

        gtv1.setText(group.getCategories());
        gtv2.setText(group.getDescription());
        gtv3.setText(String.valueOf(group.getTopicCount()));
        gtv4.setText(String.valueOf(group.getPostCount()));
        Log.w(TAG, "result: " + millis + ", " + cmseconds);
        Log.w(TAG,"result dateutil: "+ DateUtils.getRelativeTimeSpanString(millis, cmseconds, DateUtils.HOUR_IN_MILLIS).toString() );
        gtv5.setText(DateUtils.getRelativeTimeSpanString(millis, cmseconds, DateUtils.HOUR_IN_MILLIS).toString());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private String getStringResourceByName(String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (Exception e) {
            Log.w(TAG,"error: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}

