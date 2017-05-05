package com.entertainerJatt.app.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.entertainerJatt.app.android.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sony on 3/27/2017.
 */
//username=tmp-208003829&token=Y5PRgfmQhtNQ2Gv&node_type=album&start=0&limit=10&upcoming_only=0
public class DatabaseHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "EntertainerJatt";

    // Contacts table name
    private static final String TABLE_SONGS = "VideoSongs";
    private static final String FAVORITES = "FAVORITES";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String VIDEO_ID = "videoId";
    private static final String KEY_NODETYPE = "Node_Type";
    private static final String KEY_TIME = "time";
    private static final String KEY_JSON = "json";
    private static final String KEY_FAV = "FAV";
    String CREATE_CONTACTS_TABLE = "CREATE TABLE "
            + TABLE_SONGS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NODETYPE + " TEXT,"
            + KEY_JSON + " TEXT,"
            + VIDEO_ID + " TEXT,"
            + KEY_FAV + " TEXT,"
            + KEY_TIME + " TEXT" + ")";


    String CREATE_FAV_TABLE = "CREATE TABLE "
            + FAVORITES + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NODETYPE + " TEXT,"
            + KEY_JSON + " TEXT,"
            + VIDEO_ID + " TEXT,"
            + KEY_FAV + " TEXT,"
            + KEY_TIME + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);

        // Create tables again
        onCreate(db);
    }


    public void addSONGS(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NODETYPE, userInfo.getNode_type());
        values.put(KEY_JSON, userInfo.getJsonString());
        values.put(KEY_TIME, userInfo.getTime());
        values.put(VIDEO_ID, userInfo.getVideoId());
        values.put(KEY_FAV, userInfo.getFav());

        // Inserting Row
        db.insert(TABLE_SONGS, null, values);
        db.close(); // Closing database connection
    }

    public void addFav(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NODETYPE, userInfo.getNode_type());
        values.put(KEY_JSON, userInfo.getJsonString());
        values.put(KEY_TIME, userInfo.getTime());
        values.put(VIDEO_ID, userInfo.getVideoId());
        values.put(KEY_FAV, userInfo.getFav());

        // Inserting Row
        db.insert(FAVORITES, null, values);
        db.close(); // Closing database connection
    }


    // Updating single song
    public int updateContact(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NODETYPE, userInfo.getNode_type());
        values.put(KEY_JSON, userInfo.getJsonString());
        values.put(KEY_TIME, userInfo.getTime());
        values.put(VIDEO_ID, userInfo.getVideoId());
        values.put(KEY_FAV, userInfo.getFav());

        // updating row
        return db.update(TABLE_SONGS, values, VIDEO_ID + " = ?",
                new String[]{String.valueOf(userInfo.getVideoId())});
    }

    // Updating single song
    public int updateFav(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NODETYPE, userInfo.getNode_type());
        values.put(KEY_JSON, userInfo.getJsonString());
        values.put(KEY_TIME, userInfo.getTime());
        values.put(VIDEO_ID, userInfo.getVideoId());
        values.put(KEY_FAV, userInfo.getFav());
        // updating row
        return db.update(FAVORITES, values, VIDEO_ID + " = ?",
                new String[]{String.valueOf(userInfo.getVideoId())});
    }


    // Getting All Songs
    public List<UserInfo> getAllSongs() {
        List<UserInfo> songsList = new ArrayList<UserInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONGS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserInfo UserInfo = new UserInfo();
//                UserInfo.setID(Integer.parseInt(cursor.getString(0)));
                UserInfo.setNode_type(cursor.getString(1));
                UserInfo.setJsonString(cursor.getString(2));
                UserInfo.setTime(cursor.getString(5));
                UserInfo.setVideoId(cursor.getString(3));
                UserInfo.setFav(cursor.getString(4));
                // Adding contact to list
                songsList.add(UserInfo);
            } while (cursor.moveToNext());
        }
        // return song list
        return songsList;
    }// Getting All Songs



    // Getting All Songs
    public List<UserInfo> getAllFav() {
        List<UserInfo> songsList = new ArrayList<UserInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + FAVORITES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserInfo UserInfo = new UserInfo();
//                UserInfo.setID(Integer.parseInt(cursor.getString(0)));
                UserInfo.setNode_type(cursor.getString(1));
                UserInfo.setJsonString(cursor.getString(2));
                UserInfo.setTime(cursor.getString(5));
                UserInfo.setVideoId(cursor.getString(3));
                UserInfo.setFav(cursor.getString(4));
                // Adding contact to list
                songsList.add(UserInfo);
            } while (cursor.moveToNext());
        }

        // return song list
        return songsList;
    }// Getting All Songs



}
