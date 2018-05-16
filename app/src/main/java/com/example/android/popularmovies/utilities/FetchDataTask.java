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

import android.os.AsyncTask;

import com.example.android.popularmovies.model.Movie;

import java.net.URL;

/**
 * A Separate Background Task to fetch Data from the Internet.
 * Makes the Code more maintainable.
 */
public class FetchDataTask extends AsyncTask<String, Void, Movie[]> {

    // Member Variable - Holds a reference to the External Handler.
    private final OnFetchDataTaskListener<Movie[]> mFetchDataTaskListener;

    // Public Constructor - Sets the External Handler.
    public FetchDataTask(OnFetchDataTaskListener<Movie[]> fetchDataTaskListener) {
        this.mFetchDataTaskListener = fetchDataTaskListener;
    }

    // Interface Definition - Should be implemented by an External Listener.
    public interface OnFetchDataTaskListener<T> {
        void onTaskStart();

        void onTaskComplete(T movies);
    }

    // Invokes the External Handler before the AsyncTask Starts.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mFetchDataTaskListener.onTaskStart();
    }

    // Starts Connection & Parse Response.
    @Override
    protected Movie[] doInBackground(String... strings) {

        // If there's no sort criteria, there's nothing to look up.
        if (strings.length == 0) {
            return null;
        }

        String sortCriteria = strings[0];
        URL url = NetworkUtils.buildUrl(sortCriteria);

        try {
            String jsonString = NetworkUtils.getResponseFromHttpUrl(url);
            return JsonUtils.parseJson(jsonString);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Invokes the External Handler after the AsyncTask has completed.
    @Override
    protected void onPostExecute(Movie[] movies) {
        mFetchDataTaskListener.onTaskComplete(movies);
    }
}
