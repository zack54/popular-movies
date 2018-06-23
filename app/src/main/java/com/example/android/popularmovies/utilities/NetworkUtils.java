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

import com.example.android.popularmovies.BuildConfig;

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
    public static final String CRITERIA_KEY = "criteria";
    public static final String POPULAR_SORT_CRITERIA = "popular";
    public static final String TOP_RATED_SORT_CRITERIA = "top_rated";
    public static final String FAVORITE_CRITERIA = "favorite";

    public static final String VIDEOS_ENDPOINT = "videos";
    public static final String REVIEWS_ENDPOINT = "reviews";

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;

    // Builds the URL used to talk to the server using a API Key & a Sort Criteria.
    public static URL buildUrl(String pathToAppend, int movieId) {
        String stringUri = BASE_URL;

        switch (pathToAppend) {
            case POPULAR_SORT_CRITERIA:
                stringUri += POPULAR_SORT_CRITERIA;
                break;
            case TOP_RATED_SORT_CRITERIA:
                stringUri += TOP_RATED_SORT_CRITERIA;
                break;
            case VIDEOS_ENDPOINT:
                stringUri += movieId + "/" + VIDEOS_ENDPOINT;
                break;
            case REVIEWS_ENDPOINT:
                stringUri += movieId + "/" + REVIEWS_ENDPOINT;
                break;
        }

        Uri uri = Uri.parse(stringUri).buildUpon().appendQueryParameter(API_KEY_PARAM, API_KEY).build();

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
