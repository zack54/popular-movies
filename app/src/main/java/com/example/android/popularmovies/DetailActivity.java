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

import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.FetchImages;
import com.example.android.popularmovies.utilities.FetchReviews;
import com.example.android.popularmovies.utilities.FetchVideos;
import com.example.android.popularmovies.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays details about each Movie.
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final int FETCH_VIDEOS_LOADER_ID = 10;
    private static final int FETCH_REVIEWS_LOADER_ID = 20;

    // Member Variables - Holds references to the Views in Detail Activity Layout.
    @BindView(R.id.detail_iv_poster)
    ImageView mPosterImageView;
    @BindView(R.id.detail_tv_release_date)
    TextView mReleaseDateTextView;
    @BindView(R.id.detail_tv_vote)
    TextView mVoteTextView;
    @BindView(R.id.detail_tv_overview)
    TextView mOverviewTextView;
    @BindView(R.id.detail_lv_videos)
    ListView mVideosListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Connects member variables to views in the detail activity layout.
        ButterKnife.bind(this);

        // Adds Up Navigation Button into the Action Bar.
        addUpNavigationButton();

        // Populates the Detail Activity's Views.
        loadData();
    }


    // Helper Method - Shows the Up Navigation as an action button in the Action Bar.
    private void addUpNavigationButton() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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


    // Helper Method - Populates the detail activity's views with the movie's properties.
    private void loadData() {

        // Checks if a Movie Object is passed within the Intent, Then Populate the detail activity
        Movie movie = null;
        Intent intent = getIntent();

        if (intent == null) {
            closeActivityOnError();
        } else {
            movie = intent.getParcelableExtra("movie");
        }

        if (movie == null) {
            closeActivityOnError();
        } else {

            // Fetch the Movie Poster - Populates the Image View.
            String posterPath = movie.getmPosterPath();
            FetchImages.usingRelativePathAndSize(mPosterImageView, posterPath, FetchImages.MEDIUM_IMAGE_SIZE);

            // Fetch the Movie Videos & Reviews - Populates the correspondent UI.
            int id = movie.getmId();
            Bundle bundle = new Bundle();
            bundle.putInt(NetworkUtils.ID_KEY, id);
            getSupportLoaderManager().initLoader(FETCH_VIDEOS_LOADER_ID, bundle, this);
            getSupportLoaderManager().initLoader(FETCH_REVIEWS_LOADER_ID, bundle, this);

            this.setTitle(movie.getmOriginalTitle());
            String string = "(" + movie.getmReleaseDate() + ")";
            mReleaseDateTextView.setText(string);
            mVoteTextView.setText(String.valueOf(movie.getmVoteAverage()));
            mOverviewTextView.setText(movie.getmOverview());
        }
    }

    // Helper Method - Finishes the detail activity.
    private void closeActivityOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    // Instantiates a New Loader for given ID.
    @NonNull
    @Override
    public Loader onCreateLoader(int loaderID, @Nullable Bundle args) {

        assert args != null;
        int movieId = args.getInt(NetworkUtils.ID_KEY);

        switch (loaderID) {
            case FETCH_VIDEOS_LOADER_ID:
                return new FetchVideos(this, movieId);
            case FETCH_REVIEWS_LOADER_ID:
                return new FetchReviews(this, movieId);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderID);
        }
    }

    // Updates the UI with the Loader Results after Network has Completed.
    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        int loaderId = loader.getId();
        switch (loaderId) {
            case FETCH_VIDEOS_LOADER_ID:
                displayVideos(data);
                return;
            case FETCH_REVIEWS_LOADER_ID:
                displayReviews(data);
                return;
            default:
                throw new RuntimeException("Loader Not Implemented: " + loader);
        }
    }

    // TODO: ...
    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        int loaderId = loader.getId();
        switch (loaderId) {
            case FETCH_VIDEOS_LOADER_ID:
                // ... cast data
                return;
            case FETCH_REVIEWS_LOADER_ID:
                // ... cast data
                return;
            default:
                // ...
        }
    }

    // TODO ...
    private void displayVideos(Object data) {
        if (data != null && data instanceof String[]) {
            String[] videos = (String[]) data;
            VideosArrayAdapter adapter = new VideosArrayAdapter(this, videos);
            mVideosListView.setAdapter(adapter);

            ListAdapter listAdapter = mVideosListView.getAdapter();
            if (listAdapter == null) {
                return;
            }
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                    mVideosListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            int totalHeight = 0;
            View view = null;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                view = listAdapter.getView(i, view, mVideosListView);
                if (i == 0)
                    view.setLayoutParams(new ViewGroup.LayoutParams(
                            desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

                view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mVideosListView.getLayoutParams();
            params.height = totalHeight + (mVideosListView.getDividerHeight() * (listAdapter.getCount() - 1));
            mVideosListView.setLayoutParams(params);
        }
    }

    private void displayReviews(Object data) {
        if (data != null && data instanceof ContentValues[]) {

        }
    }
}
