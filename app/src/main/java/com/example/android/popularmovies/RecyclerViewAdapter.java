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

    private Movie[] mMovies = {};
    // Store a Reference to the External Handler
    private final OnClickListener mClickHandler;

    /**
     * Creates a RecyclerViewAdapter Instance.
     */
    public RecyclerViewAdapter(OnClickListener clickListener) {
        mClickHandler = clickListener;
    }

    public void setmMovies(Movie[] movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    /**
     * Interface Definition - it handles Click Events.
     */
    public interface OnClickListener {
        void onClick(Movie currentMovie);
    }

    /**
     * Cache of the children views for a movie list item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mMovies[getAdapterPosition()]);
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

}
