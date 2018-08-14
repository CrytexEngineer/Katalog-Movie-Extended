package com.example.aqil.favoritemovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.aqil.favoritemovie.DatabaseContract.CONTENT_URI;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    final static String EXTRA_MOVIE = "movie";
    ArrayList<Movie> listMovie = new ArrayList<>();

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    Context context;

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_layout_favorite, parent, false);
        return new ViewHolder(itemRow);
    }


    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, final int position) {
        Glide.with(context).load(listMovie.get(position).getThumbnailPath(0)).override(640, 480).into(holder.poster);
        Log.d(getClass().getSimpleName(), "onBindViewHolder: " +listMovie.get(position).getThumbnailPath(0));
        holder.title.setText(listMovie.get(position).getTitle());
        holder.releaseDate.setText(listMovie.get(position).getRelease_date());
        holder.remarks.setText(listMovie.get(position).getOverview());

    }

    @Override
    public int getItemCount() {
        Log.d(getClass().getSimpleName(), "getItemCount: " + listMovie.size());
        return listMovie.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title, remarks, releaseDate;
        Button btnMore;

        @BindView(R.id.btn_Delete)
        Button buttonDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            poster = itemView.findViewById(R.id.movie_poster);
            title = itemView.findViewById(R.id.movie_title);
            remarks = itemView.findViewById(R.id.movie_remarks);
            releaseDate = itemView.findViewById(R.id.movie_date_time);
            btnMore = itemView.findViewById(R.id.btn_more);

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(CONTENT_URI + "/" + listMovie.get(getAdapterPosition()).getId());
                    context.getContentResolver().delete(uri, null, null);
                    listMovie.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    Intent toastIntent = new Intent(context, FavoriteAdapter.class);
                    toastIntent.setAction("com.example.aqil.katalogfilmuiux.ON_CLICK_FAVORITE_ACTION");
                    context.sendBroadcast(toastIntent);

                }
            });

            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(EXTRA_MOVIE, listMovie.get(getAdapterPosition()));
                    context.startActivity(intent);

                }
            });

        }
    }

    public ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<Movie> listMovie) {
        this.listMovie = listMovie;
    }

}
