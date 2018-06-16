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

import java.net.URL;

/**
 * A Separate Background Task to Fetch Videos from the Internet.
 * Makes the Code more maintainable.
 */
public class FetchVideos extends AsyncTaskLoader<String[]> {

    // Member Variable - Holds & Caches the Result of the load.
    private String[] mVideos;
    private final int mMovieId;

    // Public Constructor.
    public FetchVideos(Context context, int movieId) {
        super(context);
        mMovieId = movieId;
    }

    // Returns the Cached Result is it exist, Otherwise Force the Load.
    @Override
    protected void onStartLoading() {
        if (mVideos != null) {
            deliverResult(mVideos);
        } else {
            forceLoad();
        }
    }

    // Starts Connection & Parse Response.
    @Override
    public String[] loadInBackground() {
        // If there's no sort criteria, there's nothing to look up.
        if (mMovieId < 0) {
            return null;
        }

        URL url = NetworkUtils.buildUrl(NetworkUtils.VIDEOS_ENDPOINT, mMovieId);

        try {
            String jsonString = NetworkUtils.getResponseFromHttpUrl(url);
            return JsonUtils.getVideosFromJson(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Caches & Sends the Result of the load to the Registered Listener.
    @Override
    public void deliverResult(String[] data) {
        mVideos = data;
        super.deliverResult(data);
    }
}
