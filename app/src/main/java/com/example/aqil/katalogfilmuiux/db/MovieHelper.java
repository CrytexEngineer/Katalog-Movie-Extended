package com.example.aqil.katalogfilmuiux.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.aqil.katalogfilmuiux.entity.Movie;

import static android.provider.BaseColumns._ID;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.TABLE_FAVORITE;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.DESCRIPTION;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.OVERVIEW;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.POSTER;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.RELEASE_DATE;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.THUMBNAIL;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.TITLE;

public class MovieHelper {
    private static String DATABASE_TABLE = TABLE_FAVORITE;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public MovieHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;

    }

    public MovieHelper open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public int delete(int id) {
        return database.delete(TABLE_FAVORITE, _ID + " = '" + id + "'", null);
    }


    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null

                , null
                , null
                , _ID + " DESC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}

