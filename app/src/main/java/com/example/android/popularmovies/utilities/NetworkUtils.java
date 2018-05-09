package com.example.android.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String POPULAR_BASE_URL = "http://api.themoviedb.org/3/movie/popular";
    private static final String TOP_RATED_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated";

    private static final String API_KEY_PARAM = "api_key";
    private static final String key = "ff8bcb7ef6696d53cc8dde397d9f5368";

    public static URL buildUrl(String locationQuery) {
        Uri uri = Uri.parse(POPULAR_BASE_URL).buildUpon()
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