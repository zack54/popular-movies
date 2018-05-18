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
import com.example.android.popularmovies.utilities.FetchImages;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays details about each Movie.
 */
public class DetailActivity extends AppCompatActivity {

    // Constant - Holds the size of the images to query for the detail layout.
    private static final String IMAGE_SIZE = "w500/";

    // Member Variables - Holds references to the Views in Detail Activity Layout.
    @BindView(R.id.detail_iv_poster)
    ImageView mPosterImageView;
    @BindView(R.id.detail_tv_title)
    TextView mTitleTextView;
    @BindView(R.id.detail_tv_release_date)
    TextView mReleaseDateTextView;
    @BindView(R.id.detail_tv_vote)
    TextView mVoteTextView;
    @BindView(R.id.detail_tv_overview)
    TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Connects member variables to views in the detail activity layout.
        ButterKnife.bind(this);

        // Adds Up Navigation Button into the Action Bar.
        addUpNavigationButton();

        // Checks if a Movie Object is passed within the Intent, Then Populate the detail activity
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

    // Helper Method - Shows the Up Navigation as an action button in the Action Bar.
    private void addUpNavigationButton() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // Helper Method - Finishes the detail activity.
    private void closeActivityOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    // Helper Method - Populates the detail activity's views with the movie's properties.
    private void populateUI(Movie movie) {
        this.setTitle(movie.getmOriginalTitle());
        String posterPath = movie.getmPosterPath();
        FetchImages.usingRelativePathAndSize(mPosterImageView, posterPath, IMAGE_SIZE);
        mTitleTextView.setText(movie.getmOriginalTitle());
        String string = "(" + movie.getmReleaseDate() + ")";
        mReleaseDateTextView.setText(string);
        mVoteTextView.setText(String.valueOf(movie.getmVoteAverage()));
        mOverviewTextView.setText(movie.getmOverview());
    }

    // Navigates the Main Activity when Up Button is clicked.
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

}
