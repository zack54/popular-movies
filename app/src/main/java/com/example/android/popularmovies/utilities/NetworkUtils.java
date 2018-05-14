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

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Utility functions - Handles communication with the server.
 */
public final class NetworkUtils {

    // Constants - Used To Avoid Errors that come with Typing Long URLs.
    private static final String POPULAR_SORT_CRITERIA = "popular";
    private static final String TOP_RATED_SORT_CRITERIA = "top_rated";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "api_key";

    // TODO: insert an "themoviedb.org" API key.
    private static final String KEY = "ff8bcb7ef6696d53cc8dde397d9f5368";

    public static String getPopularSortCriteria() {
        return POPULAR_SORT_CRITERIA;
    }

    public static String getTopRatedSortCriteria() {
        return TOP_RATED_SORT_CRITERIA;
    }

    /**
     * Builds the URL used to talk to the server using a API Key & a Sort Criteria.
     *
     * @param sortCriteria The Sort Criteria that will be queried for.
     * @return The URL to use to query the server.
     */
    public static URL buildUrl(String sortCriteria) {

        String stringUri = BASE_URL;
        if (sortCriteria.equals(POPULAR_SORT_CRITERIA)) {
            stringUri += POPULAR_SORT_CRITERIA;
        } else if (sortCriteria.equals(TOP_RATED_SORT_CRITERIA)) {
            stringUri += TOP_RATED_SORT_CRITERIA;
        }

        Uri uri = Uri.parse(stringUri).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Performs the HTTP Connection & Get the HTTP Response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
