package com.example.aqil.katalogfilmuiux.widget;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.aqil.katalogfilmuiux.R;
import com.example.aqil.katalogfilmuiux.adapter.MovieAdapter;
import com.example.aqil.katalogfilmuiux.entity.Movie;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.CONTENT_URI;


class StackRemoteViewsFactory implements
        RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Movie> mWidgetItems = new ArrayList<>();
    private Context mContext;

    StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        int mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {


    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems.clear();
        final long identityToken = Binder.clearCallingIdentity();
        Cursor cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor);
                mWidgetItems.add(movie);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }

        if (cursor != null) {
            cursor.close();
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Movie currentMovie ;
        Bundle extras = new Bundle();
        Bitmap bmp = null;
        String releaseDate = null;
        try {
            currentMovie=mWidgetItems.get(position);
            bmp = Glide.with(mContext)
                    .load(currentMovie.getThumbnailPath(0)).asBitmap()
                    .error(new ColorDrawable(mContext.getResources().getColor(R.color.colorPrimaryDark)))
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

            releaseDate = currentMovie.getRelease_date();
            extras.putString(MovieAdapter.EXTRA_MOVIE,currentMovie.getTitle());

        } catch (InterruptedException | ExecutionException | IndexOutOfBoundsException e) {
            Log.d("Widget Load Error", "error");
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, bmp);
        rv.setTextViewText(R.id.textview, releaseDate);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}