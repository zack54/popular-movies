/*
 * Copyright (c) 2018. Issam ELouaaer
 *
 * Licensed under the  GNU GENERAL PUBLIC  License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/gpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.FetchPosters;

/**
 * Exposes a list of Movies.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    // Member Variable - Stores a Reference to the Click Events External Handler.
    private final OnClickListener mClickHandler;

    // Member Variable - Stores the List of Movies.
    private Movie[] mMovies;

    // Constructor - Initializes the Click Events External Handler.
    MoviesAdapter(OnClickListener clickListener) {
        mClickHandler = clickListener;
    }

    // Sets the Movies Data Source & Notifies the Adapter that Data has changed.
    public void setmMovies(Movie[] movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    // Initializes a ViewHolder using the Item's "XML" Layout.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.main_poster_item, parent, false);
        return new ViewHolder(view);
    }

    // Populates & Binds a ViewHolder with the correct Movie's Image.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovies[position];
        String posterRelativePath = movie.getmPosterPath();
        ImageView imageView = holder.posterImageView;
        FetchPosters.usingRelativePathAndSize(imageView, posterRelativePath, FetchPosters.MEDIUM_IMAGE_SIZE);
    }

    // Returns the Total Number of Movies.
    @Override
    public int getItemCount() {
        if (mMovies != null) return mMovies.length;
        return 0;
    }

    // Interface Definition - should be implemented by external component to handles Click Events.
    public interface OnClickListener {
        void onClick(Movie currentMovie);
    }

    // Caches Views for Movies item to be reused when needed.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Holds references to Sub-View within a List Item View.
        final ImageView posterImageView;

        ViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.main_poster);
            itemView.setOnClickListener(this);
        }

        // Passes the current clicked movie to the External Clicks handler.
        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mMovies[getAdapterPosition()]);
        }
    }

}
