package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.FetchImage;

public class DetailActivity extends AppCompatActivity {

    private static final String IMAGE_SIZE = "w500/";

    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteTextView;
    private TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPosterImageView = findViewById(R.id.detail_iv_poster);
        mTitleTextView = findViewById(R.id.detail_tv_title);
        mReleaseDateTextView = findViewById(R.id.detail_tv_release_date);
        mVoteTextView = findViewById(R.id.detail_tv_vote);
        mOverviewTextView = findViewById(R.id.detail_tv_overview);

        addUpNavigationButton();

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

    private void addUpNavigationButton() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
