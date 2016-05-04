package com.trizelka.myforum.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserData {

    public static final String PREFS_NAME = "USER_DATA";
    public static final String name = "NAME";
    public static final String email = "EMAIL";
    public static final String uriImage = "URI_IMAGE";
    public static final String token = "TOKEN";
    public static final String _id = "ID_USER";


    public static String getUri(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(uriImage, "");
    }
    public static String getName(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(name, "");
    }

    public static String getEmail(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(email, "");
    }

    public static String getToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(token, "");
    }

    public static String getId(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(_id, "");
    }

    public static void saveUri(Context context, String ip) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(uriImage, ip);
        editor.commit();
    }

    public static void saveName(Context context, String port) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, port);
        editor.commit();
    }

    public static void saveEmail(Context context, String port) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(email, port);
        editor.commit();
    }

    public static void saveToken(Context context, String port) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(token, port);
        editor.commit();
    }

    public static void saveId(Context context, String port) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(_id, port);
        editor.commit();
    }

}
