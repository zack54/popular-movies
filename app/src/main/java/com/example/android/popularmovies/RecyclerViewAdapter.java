package com.example.android.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * {@link RecyclerViewAdapter} exposes a list of Movies
 * {@link android.support.v7.widget.RecyclerView}
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SIZE = "w342/";

    // private Movie[] mMovies = {};
    private Movie[] mMovies = {
            new Movie(8.6,
                    "/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg",
                    "Avengers: Infinity War",
                    "As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.",
                    "2018-04-25")
    };


    /**
     * Creates a RecyclerViewAdapter Instance.
     */
    public RecyclerViewAdapter() {
    }

    /**
     * Cache of the children views for a movie list item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_poster);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovies[position];
        String posterPath = movie.getmPosterPath();
        String fullPosterPath = BASE_URL + SIZE + posterPath;
        Picasso.get().load(fullPosterPath).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mMovies.length;
    }

    public void setmMovies(Movie[] movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }
}
