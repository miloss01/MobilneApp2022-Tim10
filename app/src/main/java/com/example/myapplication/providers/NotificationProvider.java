package com.example.myapplication.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;

import android.database.Cursor;
import android.database.SQLException;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import android.net.Uri;

import java.util.HashMap;

public class NotificationProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.example.myapplication.providers.NotificationProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/notifications";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    public static final String MESSAGE = "message";
    public static final String RECEIVER_ID = "receiver_id";
    public static final String TIME_OF_RECEIVING = "time";

    private static HashMap<String, String> NOTIFICATIONS_PROJECTION_MAP;

    static final int NOTIFICATIONS = 1;
    static final int NOTIFICATION_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "notifications", NOTIFICATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "notifications/#", NOTIFICATION_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Notifications";
    static final String NOTIFICATIONS_TABLE_NAME = "notifications";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + NOTIFICATIONS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " message TEXT NOT NULL, " +
                    " receiver_id TEXT NOT NULL, " +
                    " time TEXT NOT NULL);";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATIONS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = db.insert(NOTIFICATIONS_TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(NOTIFICATIONS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case NOTIFICATIONS:
                qb.setProjectionMap(NOTIFICATIONS_PROJECTION_MAP);
                break;

            case NOTIFICATION_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        if (uriMatcher.match(uri) == NOTIFICATIONS) {
            count = db.delete(NOTIFICATIONS_TABLE_NAME, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        if (uriMatcher.match(uri) == NOTIFICATIONS) {
            count = db.update(NOTIFICATIONS_TABLE_NAME, values, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
//        switch (uriMatcher.match(uri)){
//
//            case NOTIFICATIONS:
//                return "vnd.android.cursor.dir/vnd.example.students";
//
//            case NOTIFICATION_ID:
//                return "vnd.android.cursor.item/vnd.example.students";
//            default:
//                throw new IllegalArgumentException("Unsupported URI: " + uri);
//        }
        return null;
    }

    public void resetDatabase() {
        db.close();
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }
}