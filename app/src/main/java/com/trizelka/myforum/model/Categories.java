package com.trizelka.myforum.model;

import java.util.ArrayList;

public class Categories {

    private String categories;
    private int cid,post_count,topic_count;
    private String loggedin, timestamp_lastpost;
    private String icon,bgcolor,slug,tslug,fontcolor,description;
    private ArrayList<Topics> topics;


    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getTopicCount() {
        return topic_count;
    }

    public void setTopicCount(int topic_count) {
        this.topic_count = topic_count;
    }

    public int getPostCount() {
        return post_count;
    }

    public void setPostCount(int post_count) {
        this.post_count = post_count;
    }

    public String getTeaserTimestamp_lastpost() {
        return timestamp_lastpost;
    }

    public void setTeaserTimestamp_lastpost(String timestamp_lastpost) {
        this.timestamp_lastpost = timestamp_lastpost;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTeaserSlug() {
        return tslug;
    }

    public void setTeaserSlug(String tslug) {
        this.tslug = tslug;
    }

    public String getFontColor() {
        return fontcolor;
    }

    public void setFontColor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public String getLoggedin() {
        return loggedin;
    }

    public void setLoggedin(String loggedin) {
        this.loggedin = loggedin;
    }

    public ArrayList<Topics> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<Topics> topics) {
        this.topics = topics;
    }

}