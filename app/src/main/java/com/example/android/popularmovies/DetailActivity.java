package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.FetchImage;

public class DetailActivity extends AppCompatActivity {

    // TODO: Add Up/Home Button

    private static final String IMAGE_SIZE = "w500/";

    ImageView mPosterImageView;
    TextView mTitleTextView;
    TextView mReleaseDateTextView;
    TextView mVoteTextView;
    TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPosterImageView = findViewById(R.id.detail_iv_poster);
        mTitleTextView = findViewById(R.id.detail_tv_title);
        mReleaseDateTextView = findViewById(R.id.detail_tv_release_date);
        mVoteTextView = findViewById(R.id.detail_tv_vote);
        mOverviewTextView = findViewById(R.id.detail_tv_overview);

        Intent intent = getIntent();
        if (intent == null) {
            closeActivityOnError();
        } else {

            Movie movie = intent.getParcelableExtra("movie");

            if (movie == null) {
                closeActivityOnError();
            } else {
                populateUI(movie);

            }
        }
    }

    private void closeActivityOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie movie) {

        this.setTitle(movie.getmOriginalTitle());
        String posterPath = movie.getmPosterPath();
        FetchImage.usingPathAndSize(mPosterImageView, posterPath, IMAGE_SIZE);
        mTitleTextView.setText(movie.getmOriginalTitle());
        String string = "(" + movie.getmReleaseDate() + ")";
        mReleaseDateTextView.setText(string);
        mVoteTextView.setText(String.valueOf(movie.getmVoteAverage()));
        mOverviewTextView.setText(movie.getmOverview());
    }
}
