package com.trizelka.myforum.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Trisia Juniarto on 9/22/2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "myforum.db";
    private static final int DATABASE_VERSION = 1;

    static String TABLE_USER="user";

    //User Column _id, firstname, lastname, name, email, gender, birthdate, country, profesi, updated;
    public static String CREATE_TABLE_USER ="CREATE TABLE "+TABLE_USER+" (id integer primary key autoincrement, _id text, firstname text, lastname text, name text, email text, gender text, birthdate date, country text, profesi text, updated datetime);";

    String DROP_TABLE_USER = "DROP TABLE IF EXISTS "+ TABLE_USER+ ";";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w(TAG, CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_USER);
        Log.w(TAG, DROP_TABLE_USER);
        onCreate(db);
    }

}