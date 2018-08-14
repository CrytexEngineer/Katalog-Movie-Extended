package com.example.aqil.favoritemovie;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_FAVORITE ="favorite";
    public static final class favoriteColumns implements BaseColumns{
        public static String TITLE="title";
        public static String DESCRIPTION="description";
        public static String POSTER ="poster";
        public static String RELEASE_DATE ="release_date";
        public static String THUMBNAIL= "thumbnail";
        public static String OVERVIEW ="overview";
    }

    public static final String AUTHORITY ="com.example.aqil.katalogfilmuiux";
    public static final Uri CONTENT_URI= new Uri.Builder().scheme("content").authority(AUTHORITY).appendPath(TABLE_FAVORITE).build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }
}
