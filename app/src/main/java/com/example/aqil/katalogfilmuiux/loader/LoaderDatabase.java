package com.example.aqil.katalogfilmuiux.loader;
import android.content.Context;
import android.database.Cursor;
import  android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.aqil.katalogfilmuiux.entity.Movie;

import java.util.ArrayList;

import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.CONTENT_URI;

public class LoaderDatabase extends AsyncTaskLoader<ArrayList<Movie>> {


    public LoaderDatabase(Context context) {
        super(context);
        onContentChanged();
        Log.d(getClass().getSimpleName(), "LoaderDatabase: "+"LOAD");
    }

    @Override
    protected void onStartLoading() {
        Log.d(getClass().getSimpleName(), "onStartLoading: " +"EXECUTE");
        super.onStartLoading();
        if (takeContentChanged())
            forceLoad();

    }

    @Override
    public ArrayList<Movie> loadInBackground() {

        ArrayList<Movie> list = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(CONTENT_URI, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor);
                list.add(movie);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }

        if (cursor != null) {
            cursor.close();
        }
        Log.d(getClass().getSimpleName(), "loadInBackground: "+list.size());
        return list;

    }

    @Override
    public void deliverResult(ArrayList<Movie> data) {
        super.deliverResult(data);

    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

    }

}
