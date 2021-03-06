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

import android.content.ContentValues;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions - Handles JSON data.
 */
public class JsonUtils {

    // Constants - Holds the keys needed to extract the info from JSON String.
    private static final String REVIEW_RESULTS = "results";
    public static final String REVIEW_AUTHOR = "author";
    public static final String REVIEW_CONTENT = "content";

    private static final String VIDEO_RESULTS = "results";
    private static final String VIDEO_KEY = "key";

    private static final String MOVIE_RESULTS = "results";
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_VOTE_AVERAGE = "vote_average";
    public static final String MOVIE_POSTER_PATH = "poster_path";
    public static final String MOVIE_ORIGINAL_TITLE = "original_title";
    public static final String MOVIE_OVERVIEW = "overview";
    public static final String MOVIE_RELEASE_DATE = "release_date";

    public static final String MOVIE_POSTER = "poster";

    // Parses JSON String and returns an Array of Bundle Movies.
    public static Bundle[] getMoviesBundlesFromJson(String json) throws JSONException {

        // Local Variable - Holds an Array of Movies.
        Bundle[] movies;

        if (json == null) {
            return null;
        }

        // Gets the List of Movies from the JSON Object.
        JSONObject jsonObject = new JSONObject(json);
        JSONArray resultsMovies = jsonObject.getJSONArray(MOVIE_RESULTS);

        // Extracts each Movie's Properties & Adds a Movie Object to the Array of Movies.
        movies = new Bundle[resultsMovies.length()];
        for (int i = 0; i < resultsMovies.length(); i++) {

            JSONObject movieJSONObject = resultsMovies.getJSONObject(i);
            int id = movieJSONObject.getInt(MOVIE_ID);
            double voteAverage = movieJSONObject.getDouble(MOVIE_VOTE_AVERAGE);
            String posterPath = movieJSONObject.getString(MOVIE_POSTER_PATH);
            String originalTitle = movieJSONObject.getString(MOVIE_ORIGINAL_TITLE);
            String overview = movieJSONObject.getString(MOVIE_OVERVIEW);
            String releaseDate = movieJSONObject.getString(MOVIE_RELEASE_DATE);

            Bundle movieBundle = new Bundle();
            movieBundle.putInt(MOVIE_ID, id);
            movieBundle.putDouble(MOVIE_VOTE_AVERAGE, voteAverage);
            movieBundle.putString(MOVIE_POSTER_PATH, posterPath);
            movieBundle.putString(MOVIE_ORIGINAL_TITLE, originalTitle);
            movieBundle.putString(MOVIE_OVERVIEW, overview);
            movieBundle.putString(MOVIE_RELEASE_DATE, releaseDate);

            movies[i] = movieBundle;
        }
        return movies;
    }

    public static String[] getVideosFromJson(String json) throws JSONException {

        // Local Variable - Holds an Array of Videos.
        String[] videos;

        if (json == null) {
            return null;
        }

        // Gets the List of Videos from the JSON Object.
        JSONObject jsonObject = new JSONObject(json);
        JSONArray resultsVideos = jsonObject.getJSONArray(VIDEO_RESULTS);

        // Extracts each Video's Key & Adds it to the Array of Videos.
        videos = new String[resultsVideos.length()];
        for (int i = 0; i < resultsVideos.length(); i++) {

            JSONObject videoJSONObject = resultsVideos.getJSONObject(i);
            String video = videoJSONObject.getString(VIDEO_KEY);

            videos[i] = video;
        }

        return videos;
    }

    public static ContentValues[] getReviewsFromJson(String json) throws JSONException {

        // Local Variable - Holds an Array of Reviews.
        ContentValues[] reviews;

        if (json == null) {
            return null;
        }

        // Gets the List of Reviews from the JSON Object.
        JSONObject jsonObject = new JSONObject(json);
        JSONArray resultsReviews = jsonObject.getJSONArray(REVIEW_RESULTS);

        // Extracts each Review's Properties & Adds them to the Array of Reviews.
        reviews = new ContentValues[resultsReviews.length()];
        for (int i = 0; i < resultsReviews.length(); i++) {

            JSONObject reviewJSONObject = resultsReviews.getJSONObject(i);
            String author = reviewJSONObject.getString(REVIEW_AUTHOR);
            String content = reviewJSONObject.getString(REVIEW_CONTENT);

            ContentValues review = new ContentValues();
            review.put(REVIEW_AUTHOR, author);
            review.put(REVIEW_CONTENT, content);

            reviews[i] = review;
        }

        return reviews;
    }
}