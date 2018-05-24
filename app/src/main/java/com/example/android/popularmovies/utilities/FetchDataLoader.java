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

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovies.data.Movie;

import java.net.URL;

/**
 * A Separate Background Task to fetch Data from the Internet.
 * Makes the Code more maintainable.
 */
public class FetchDataLoader extends AsyncTaskLoader<Movie[]> {

    // Member Variable - Holds & Caches the Result of the load.
    private Movie[] mMovies;
    private final String mSortCriteria;

    // Public Constructor - Sets the Sort Criteria.
    public FetchDataLoader(Context context, String sortCriteria) {
        super(context);
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
    public Movie[] loadInBackground() {
        // If there's no sort criteria, there's nothing to look up.
        if (mSortCriteria == null) {
            return null;
        }

        URL url = NetworkUtils.buildUrl(mSortCriteria);

        try {
            String jsonString = NetworkUtils.getResponseFromHttpUrl(url);
            return JsonUtils.parseJson(jsonString);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Caches & Sends the Result of the load to the Registered Listener.
    @Override
    public void deliverResult(Movie[] data) {
        mMovies = data;
        super.deliverResult(data);
    }
}
