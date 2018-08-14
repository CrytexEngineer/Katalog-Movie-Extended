package com.example.aqil.katalogfilmuiux.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.example.aqil.katalogfilmuiux.scheduler.AlarmScheduler;
import com.example.aqil.katalogfilmuiux.entity.Movie;
import com.example.aqil.katalogfilmuiux.loader.LoaderHTTPClient;

import java.util.ArrayList;

public class GetReleaseMovieService extends IntentService {


    ResultReceiver receiver;

    public GetReleaseMovieService() {
        super("GetReleaseMovieIntentService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            receiver = intent.getParcelableExtra(AlarmScheduler.EXTRA_RECEIVER);
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<Movie> list = new LoaderHTTPClient(getBaseContext(), null).loadInBackground();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(AlarmScheduler.EXTRA_LIST, list);
            receiver.send(0, bundle);


        }

    }

}
