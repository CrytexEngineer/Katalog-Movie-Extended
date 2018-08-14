package com.example.aqil.katalogfilmuiux;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aqil.katalogfilmuiux.adapter.FavoriteAdapter;
import com.example.aqil.katalogfilmuiux.adapter.MovieAdapter;
import com.example.aqil.katalogfilmuiux.entity.Movie;
import com.example.aqil.katalogfilmuiux.widget.ImagesBannerWidget;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.CONTENT_URI;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.DESCRIPTION;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.OVERVIEW;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.POSTER;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.RELEASE_DATE;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.THUMBNAIL;
import static com.example.aqil.katalogfilmuiux.db.DatabaseContract.favoriteColumns.TITLE;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.img_detail)
    ImageView imgDetail;
    @BindView(R.id.titleDetail)
    TextView titleDetail;
    @BindView(R.id.movie_date_time_detail)
    TextView movieDateDetail;
    @BindView(R.id.description)
    TextView detaiLDescription;
    @BindView(R.id.favorites_checkbox)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        int requestCode = getIntent().getIntExtra(MovieAdapter.EXTRA_REQUEST_CODE, 0);
        ButterKnife.bind(this);
        final Movie currentMovie = getIntent().getParcelableExtra(MovieAdapter.EXTRA_MOVIE);
        Picasso.get().load(currentMovie.getPosterPath(0)).resize(480, 680).into(imgDetail);
        movieDateDetail.setText(currentMovie.getRelease_date());
        if (requestCode != 1) {
            button.setVisibility(View.INVISIBLE);
            detaiLDescription.setText(currentMovie.getDescription(0));
        } else detaiLDescription.setText(currentMovie.getDescription(1));
        button.setOnClickListener(
                new View.OnClickListener() {


                    public void onClick(View v) {
                        Boolean hasAdded = false;
                        ArrayList<Movie> listFavoritedMovie = new ArrayList<>();
                        Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                Movie movie = new Movie(cursor);
                                listFavoritedMovie.add(movie);
                                cursor.moveToNext();
                            }
                            while (!cursor.isAfterLast());
                        }

                        if (cursor != null) {
                            cursor.close();
                        }

                        final Toast hasAdedToast = Toast.makeText(DetailActivity.this, R.string.restrict_add_movie, Toast.LENGTH_SHORT);
                        Toast addFavoriteToast = Toast.makeText(DetailActivity.this, R.string.success_add_movie, Toast.LENGTH_SHORT);
                        for (int i = 0; i < listFavoritedMovie.size(); i++) {
                            if (currentMovie.getTitle().equals(listFavoritedMovie.get(i).getTitle())) {
                                hasAdedToast.show();
                                hasAdded = true;

                            }
                        }
                        if (!hasAdded) {

                            ContentValues contentValues = new ContentValues();
                            contentValues.put(TITLE, currentMovie.getTitle());
                            contentValues.put(OVERVIEW, currentMovie.getOverview());
                            contentValues.put(POSTER, currentMovie.getPosterPath(1));
                            contentValues.put(RELEASE_DATE, currentMovie.getRelease_date());
                            contentValues.put(THUMBNAIL, currentMovie.getThumbnailPath(1));
                            contentValues.put(DESCRIPTION, currentMovie.getDescription(1));
                            getContentResolver().insert(CONTENT_URI, contentValues);
                            Intent toastIntent = new Intent(DetailActivity.this, ImagesBannerWidget.class);
                            toastIntent.setAction(ImagesBannerWidget.ON_CLICK_FAVORITE_ACTION);
                            sendBroadcast(toastIntent);
                            addFavoriteToast.show();

                        }

                    }
                }
        );

    }
}
