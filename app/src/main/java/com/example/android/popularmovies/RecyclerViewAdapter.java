package com.example.android.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.FetchImage;

/**
 * {@link RecyclerViewAdapter} exposes a list of Movies
 * {@link android.support.v7.widget.RecyclerView}
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String IMAGE_SIZE = "w342/";

    private Movie[] mMovies;
    // Store a Reference to the External Handler
    private final OnClickListener mClickHandler;

    /**
     * Creates a RecyclerViewAdapter Instance.
     */
    RecyclerViewAdapter(OnClickListener clickListener) {
        mClickHandler = clickListener;
    }

    public void setmMovies(Movie[] movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    public Movie[] getmMovies() {
        return mMovies;
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

        final ImageView mImageView;

        ViewHolder(View itemView) {
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
        ImageView imageView = holder.mImageView;
        FetchImage.usingPathAndSize(imageView, posterPath, IMAGE_SIZE);
    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.length;
        }
        return 0;
    }

}
