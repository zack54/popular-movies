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

import com.example.android.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions - Handles JSON data.
 */
class JsonUtils {

    /**
     * Parses JSON String and returns an Array of Movies.
     *
     * @param json JSON String.
     * @return Array of Movies Objects.
     * @throws JSONException If JSON data cannot be properly parsed.
     */
    public static Movie[] parseJson(String json) throws JSONException {

        // Constants - Holds the keys needed to extract the info from JSON String.
        final String MOVIE_RESULTS = "results";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";

        // Local Variable - Holds an Array of Movies.
        Movie[] movies;

        if (json == null) {
            return null;
        }

        // Gets the List of Movies from the JSON Object.
        JSONObject jsonObject = new JSONObject(json);
        JSONArray resultsMovies = jsonObject.getJSONArray(MOVIE_RESULTS);

        // Extracts each Movie's Properties & Adds a Movie Object to the Array of Movies.
        movies = new Movie[resultsMovies.length()];
        for (int i = 0; i < resultsMovies.length(); i++) {

            JSONObject movieJSONObject = resultsMovies.getJSONObject(i);
            double voteAverage = movieJSONObject.getDouble(MOVIE_VOTE_AVERAGE);
            String posterPath = movieJSONObject.getString(MOVIE_POSTER_PATH);
            String originalTitle = movieJSONObject.getString(MOVIE_ORIGINAL_TITLE);
            String overview = movieJSONObject.getString(MOVIE_OVERVIEW);
            String releaseDate = movieJSONObject.getString(MOVIE_RELEASE_DATE);

            movies[i] = new Movie(voteAverage, posterPath, originalTitle, overview, releaseDate);
        }

        return movies;
    }

}