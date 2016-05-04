package com.trizelka.myforum.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.trizelka.myforum.Constants;

/**
 * Created by Trisia Juniarto on 9/22/2015.
 */
public class DataProvider extends ContentProvider {
    private static final String TAG = "DataProvider";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_LOKASI = "lokasi";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_UPDATED = "updated";

    //Column _id, firstname, lastname, name, email, gender, birthdate, country, profesi, updated;
    public static final String TABLE_USER = "user";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_BIRTHDATE = "birthdate";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_PROFESI = "profesi";

    private DbHelper dbHelper;

    public static final Uri CONTENT_URI_USER = Uri.parse("content://com.trizelka.myforum.provider/user");

    private static final int USER_ALLROWS = 1;
    private static final int USER_SINGLE_ROW = 2;

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        //Log.w(TAG,DbHelper.CREATE_TABLE_USER);
        return true;
    }

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.trizelka.myforum.provider", "user", USER_ALLROWS);
        uriMatcher.addURI("com.trizelka.myforum.provider", "user/*", USER_SINGLE_ROW);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri)) {
            case USER_ALLROWS:
                qb.setTables(getTableName(uri));
                break;
            case USER_SINGLE_ROW:
                qb.setTables(getTableName(uri));
                qb.appendWhere(COLUMN_EMAIL + "=" + "\"" + uri.getLastPathSegment().toString() + "\"");
                break;

            default:
                //return null;
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        long id;
        switch(uriMatcher.match(uri)) {
            case USER_ALLROWS:
                Cursor cursor=db.query(getTableName(uri), null, COLUMN_ID + "=?", new String[]{values.getAsString(COLUMN_ID)}, null, null, null);
                id = db.replaceOrThrow(getTableName(uri), null, values);
                Log.w("DataProvider", "insert or replace row "+ getTableName(uri));
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Uri insertUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(insertUri, null);
        return insertUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int count;
        switch(uriMatcher.match(uri)) {
            case USER_ALLROWS:
                count = db.update(getTableName(uri), values, selection, selectionArgs);
                break;

            case USER_SINGLE_ROW:
                count = db.update(getTableName(uri), values, COLUMN_EMAIL + "="+ "\"" + "?" + "\"", new String[]{uri.getLastPathSegment()});
                break;
            default:
                //return 0;
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int count;
        switch(uriMatcher.match(uri)) {
            case USER_ALLROWS:
                count = db.delete(getTableName(uri), selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private String getTableName(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case USER_ALLROWS:
            case USER_SINGLE_ROW:
                return TABLE_USER;
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch(uriMatcher.match(uri)) {
            case USER_ALLROWS:
                db.delete(TABLE_USER, null, null);
        }
        return null;
    }

}