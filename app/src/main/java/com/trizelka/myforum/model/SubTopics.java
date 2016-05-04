package com.trizelka.myforum.model;

public class SubTopics {
    private String title,slug,lastposttimeISO;
    private String topic_url,category_name,topic_create_at;
    private String user_post,status,content_post,relativeTime,icon_text_post,icon_bgcolor_post;
    private int cid, categories_post_count,user_post_count,viewcount,reputation,lastonline;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastposttimeISO() {
        return lastposttimeISO;
    }

    public void setLastposttimeISO(String lastposttimeISO) {
        this.lastposttimeISO = lastposttimeISO;
    }

    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String category_name) {
        this.category_name = category_name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTopic_url() {
        return topic_url;
    }

    public void setTopic_url(String topic_url) {
        this.topic_url = topic_url;
    }

    public String getTopic_create_at() {
        return topic_create_at;
    }

    public void setTopic_create_at(String topic_create_at) {
        this.topic_create_at = topic_create_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_post() {
        return user_post;
    }

    public void setUser_post(String user_post) {
        this.user_post = user_post;
    }

    public String getContent_post() {
        return content_post;
    }

    public void setContent_post(String content_post) {
        this.content_post = content_post;
    }

    public String getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(String relativeTime) {
        this.relativeTime = relativeTime;
    }

    public int getViewCount() {
        return viewcount;
    }

    public void setViewCount(int viewcount) {
        this.viewcount = viewcount;
    }

    public int getUserPostCount() {
        return user_post_count;
    }

    public void setUserPostCount(int user_post_count) {
        this.user_post_count = user_post_count;
    }

    public int getCategoriesPostCount() {
        return categories_post_count;
    }

    public void setCategoriesPostCount(int categories_post_count) {
        this.categories_post_count = categories_post_count;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public int getLastonline() {
        return lastonline;
    }

    public void setLastonline(int lastonline) {
        this.lastonline = lastonline;
    }

    public String getIcon_text_post() {
        return icon_text_post;
    }

    public void setIcon_text_post(String icon_text_post) {
        this.icon_text_post = icon_text_post;
    }

    public String getIcon_bgcolor_post() {
        return icon_bgcolor_post;
    }

    public void setIcon_bgcolor_post(String icon_bgcolor_post) {
        this.icon_bgcolor_post = icon_bgcolor_post;
    }
}