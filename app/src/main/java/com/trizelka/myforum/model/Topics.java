package com.trizelka.myforum.model;

public class Topics {

    private int cid;
    private String topic,slug;
    private String topic_url;
    private String topic_create_at;
    private String user_post;
    private String content_post;
    private String timestamp_lastpost;
    private String icon_text_post;
    private String icon_bgcolor_post;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    public String getTimestamp_lastpost() {
        return timestamp_lastpost;
    }

    public void setTimestamp_lastpost(String timestamp_lastpost) {
        this.timestamp_lastpost = timestamp_lastpost;
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