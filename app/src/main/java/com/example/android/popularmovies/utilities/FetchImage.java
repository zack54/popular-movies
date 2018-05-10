package com.example.android.popularmovies.utilities;

import android.widget.ImageView;

import com.example.android.popularmovies.RecyclerViewAdapter;
import com.squareup.picasso.Picasso;

public class FetchImage {

    private static final String BASE_URL = "http://image.tmdb.org/t/p/";

    public static void usingPathAndSize(ImageView imageView, String path, String size) {
        String fullPosterPath = BASE_URL + size + path;
        Picasso.get().load(fullPosterPath).into(imageView);
    }
}
