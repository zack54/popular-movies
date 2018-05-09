package com.example.android.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String POPULAR_SORT_CRITERIA = "popular";
    private static final String TOP_RATED_SORT_CRITERIA = "top_rated";

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "api_key";

    private static final String key = "ff8bcb7ef6696d53cc8dde397d9f5368";

    public static String getPopularSortCriteria() {
        return POPULAR_SORT_CRITERIA;
    }

    public static String getTopRatedSortCriteria() {
        return TOP_RATED_SORT_CRITERIA;
    }

    public static URL buildUrl(String sortCriteria) {
        String stringUri = BASE_URL;

        if (sortCriteria.equals(POPULAR_SORT_CRITERIA)) {
            stringUri += POPULAR_SORT_CRITERIA;
        } else if (sortCriteria.equals(TOP_RATED_SORT_CRITERIA)) {
            stringUri += TOP_RATED_SORT_CRITERIA;
        }

        Uri uri = Uri.parse(stringUri).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, key)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

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
