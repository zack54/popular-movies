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

package com.example.android.popularmovies.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovies.data.FavoriteMoviesContract;
import com.example.android.popularmovies.data.FavoriteMoviesContract.Movies;

import java.net.URL;

/**
 * A Separate Background Task to Fetch Movies for a specific Movie.
 * Makes the Code more maintainable.
 */
public class FetchMovies extends AsyncTaskLoader<Bundle[]> {

    // Member Variable - Holds & Caches the Result of the load.
    private Bundle[] mMovies;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private final String mSortCriteria;

    // Public Constructor.
    public FetchMovies(Context context, String sortCriteria) {
        super(context);
        mContext = context;
        mSortCriteria = sortCriteria;
    }

    // Returns the Cached Result is it exist, Otherwise Force the Load.
    @Override
    protected void onStartLoading() {
        if (mMovies != null) {
            deliverResult(mMovies);
        } else {
            forceLoad();
        }
    }

    // Starts Connection & Parse Response.
    @Override
    public Bundle[] loadInBackground() {

        // If there's no sort criteria, there's nothing to look up.
        if (mSortCriteria == null) {
            return null;
        } else if (mSortCriteria.equals(NetworkUtils.FAVORITE_CRITERIA)) {
            return getMoviesBundlesFromDatabase();
        } else {
            URL url = NetworkUtils.buildUrl(mSortCriteria, 0);
            try {
                String jsonString = NetworkUtils.getResponseFromHttpUrl(url);
                return JsonUtils.getMoviesBundlesFromJson(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    // Helper Method - Gets Movies from Local Database.
    private Bundle[] getMoviesBundlesFromDatabase() {
        Bundle[] movies = new Bundle[0];
        Cursor cursor = mContext.getContentResolver().query(FavoriteMoviesContract.Movies.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor != null) {
            movies = new Bundle[cursor.getCount()];
            if (cursor.moveToFirst()) {
                do {
                    Bundle movieBundle = new Bundle();

                    movieBundle.putInt(JsonUtils.MOVIE_ID,
                            cursor.getInt(cursor.getColumnIndex(Movies.COLUMN_ID)));
                    movieBundle.putDouble(JsonUtils.MOVIE_VOTE_AVERAGE,
                            cursor.getDouble(cursor.getColumnIndex(Movies.COLUMN_VOTE_AVERAGE)));
                    movieBundle.putByteArray(JsonUtils.MOVIE_POSTER,
                            cursor.getBlob(cursor.getColumnIndex(Movies.COLUMN_POSTER)));
                    movieBundle.putString(JsonUtils.MOVIE_ORIGINAL_TITLE,
                            cursor.getString(cursor.getColumnIndex(Movies.COLUMN_ORIGINAL_TITLE)));
                    movieBundle.putString(JsonUtils.MOVIE_OVERVIEW,
                            cursor.getString(cursor.getColumnIndex(Movies.COLUMN_OVERVIEW)));
                    movieBundle.putString(JsonUtils.MOVIE_RELEASE_DATE,
                            cursor.getString(cursor.getColumnIndex(Movies.COLUMN_RELEASE_DATE)));

                    movies[cursor.getPosition()] = movieBundle;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return movies;
    }

    // Caches & Sends the Result of the load to the Registered Listener.
    @Override
    public void deliverResult(Bundle[] data) {
        mMovies = data;
        super.deliverResult(data);
    }
}
