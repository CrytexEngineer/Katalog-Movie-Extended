package com.example.aqil.favoritemovie;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    FavoriteAdapter adapter;
    @BindView(R.id.rv_list)
    RecyclerView recyclerViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewList = findViewById(R.id.rv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new FavoriteAdapter(MainActivity.this);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter.notifyDataSetChanged();
        recyclerViewList.setAdapter(adapter);
        Bundle bundle = new Bundle();
        getSupportLoaderManager().initLoader(1, bundle, this);

    }

    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {

        return new LoaderDatabase(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {

        adapter.setListMovie(data);
        adapter.notifyDataSetChanged();
        Log.d(getClass().getSimpleName(), "onLoadFinished1: " + data.size());

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        adapter.setListMovie(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(1, null, this);
    }
}
