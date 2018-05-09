package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle JSON Data.
 */
public class JsonUtils {

    public static Movie[] parseSandwichJson(String json) throws JSONException {

        /* Constants - To hold the keys needed to extract the info from JSON String */
        final String MOVIE_RESULTS = "results";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";

        /* Local Variable - To hold each the List of Movies */
        Movie[] mMovies;

        if (json == null) {
            return null;
        }

        JSONObject jsonObject = new JSONObject(json);

        /* Extract List of Movies from the JSON Object */
        JSONArray resultsMovies = jsonObject.getJSONArray(MOVIE_RESULTS);
        mMovies = new Movie[resultsMovies.length()];

        for (int i = 0; i < resultsMovies.length(); i++) {

            /* Extract each Movie Object's Fields */
            JSONObject movieJSONObject = resultsMovies.getJSONObject(i);
            double voteAverage = movieJSONObject.getDouble(MOVIE_VOTE_AVERAGE);
            String posterPath = movieJSONObject.getString(MOVIE_POSTER_PATH);
            String originalTitle = movieJSONObject.getString(MOVIE_ORIGINAL_TITLE);
            String overview = movieJSONObject.getString(MOVIE_OVERVIEW);
            String releaseDate = movieJSONObject.getString(MOVIE_RELEASE_DATE);

            /* Add a Movie Object to the List of Movies */
            mMovies[i] = new Movie(voteAverage, posterPath, originalTitle, overview, releaseDate);
        }

        return mMovies;
    }

}