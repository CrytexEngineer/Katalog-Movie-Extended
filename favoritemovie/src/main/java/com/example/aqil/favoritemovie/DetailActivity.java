package com.example.aqil.favoritemovie;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.aqil.favoritemovie.DatabaseContract.CONTENT_URI;
import static com.example.aqil.favoritemovie.DatabaseContract.favoriteColumns.DESCRIPTION;
import static com.example.aqil.favoritemovie.DatabaseContract.favoriteColumns.OVERVIEW;
import static com.example.aqil.favoritemovie.DatabaseContract.favoriteColumns.POSTER;
import static com.example.aqil.favoritemovie.DatabaseContract.favoriteColumns.RELEASE_DATE;
import static com.example.aqil.favoritemovie.DatabaseContract.favoriteColumns.THUMBNAIL;
import static com.example.aqil.favoritemovie.DatabaseContract.favoriteColumns.TITLE;

public class DetailActivity extends AppCompatActivity {
    ImageView img_detail;
    TextView detaiLDescription;
    TextView titleDetail, movieDateDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        img_detail = findViewById(R.id.img_detail);
        titleDetail = findViewById(R.id.titleDetail);
        movieDateDetail = findViewById(R.id.movie_date_time_detail);
        detaiLDescription = findViewById(R.id.description);
        final Movie currentMovie = getIntent().getParcelableExtra(FavoriteAdapter.EXTRA_MOVIE);
        titleDetail.setText(currentMovie.getTitle());
        movieDateDetail.setText(currentMovie.getRelease_date());
        Picasso.get().load(currentMovie.getPosterPath(0)).resize(480, 680).into(img_detail);
        Log.d(getClass().getSimpleName(), "onCreate: "+currentMovie.getPosterPath(0));
        detaiLDescription.setText(currentMovie.getDescription());
        Log.d(getClass().getSimpleName(), "onCreate: "+currentMovie.getDescription());




    }
}
