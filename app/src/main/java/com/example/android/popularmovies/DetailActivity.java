package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.FetchImage;

public class DetailActivity extends AppCompatActivity {

    private static final String IMAGE_SIZE = "w500/";

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVote;
    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = findViewById(R.id.detail_iv_poster);
        mTitle = findViewById(R.id.detail_tv_title);
        mReleaseDate = findViewById(R.id.detail_tv_release_date);
        mVote = findViewById(R.id.detail_tv_vote);
        mOverview = findViewById(R.id.detail_tv_overview);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }


        Movie movie = intent.getParcelableExtra("movie");
        if (movie != null) {
            String posterPath = movie.getmPosterPath();
            FetchImage.usingPathAndSize(mPoster, posterPath, IMAGE_SIZE);
        }
        mTitle.setText(movie.getmOriginalTitle());
        mReleaseDate.setText(movie.getmReleaseDate());
        mVote.setText(String.valueOf(movie.getmVoteAverage()));
        mOverview.setText(movie.getmOverview());
    }
}
