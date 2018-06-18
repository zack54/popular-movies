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
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.popularmovies.databinding.ActivityDetailBinding;
import com.example.android.popularmovies.utilities.FetchPosters;
import com.example.android.popularmovies.utilities.FetchReviews;
import com.example.android.popularmovies.utilities.FetchVideos;
import com.example.android.popularmovies.utilities.JsonUtils;

/**
 * Displays details about each Movie.
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final int FETCH_VIDEOS_LOADER_ID = 10;
    private static final int FETCH_REVIEWS_LOADER_ID = 20;

    // Member Variable - Holds references to the Views in Detail Activity Layout.
    private ActivityDetailBinding mActivityDetailBinding;

    private VideosAdapter mVideosAdapter;
    private ReviewsAdapter mReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Data Binding - Links Views in the Detail Activity Layout UI.
        mActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        // Setups the ListViews.
        setupListViews(this);

        // Adds Up Navigation Button into the Action Bar.
        addUpNavigationButton();

        // Populates the Detail Activity's Views.
        loadData();
    }


    // Helper Method - Setups the ListViews' Adapters.
    private void setupListViews(Context context) {
        mVideosAdapter = new VideosAdapter(context);
        mActivityDetailBinding.detailVideos.setAdapter(mVideosAdapter);

        mReviewsAdapter = new ReviewsAdapter(context);
        mActivityDetailBinding.detailReviews.setAdapter(mReviewsAdapter);
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
        Bundle movieBundle = null;

        // Checks if a Movie is passed within the Intent, Then Populate the detail activity
        Intent intent = getIntent();
        if (intent == null) {
            closeActivityOnError();
        } else {
            movieBundle = intent.getExtras();
        }

        if (movieBundle == null) {
            closeActivityOnError();
        } else {

            // Fetch the Movie Poster - Populates the Image View.
            String posterPath = movieBundle.getString(JsonUtils.MOVIE_POSTER_PATH);
            FetchPosters.usingRelativePathAndSize(
                    mActivityDetailBinding.detailPoster, posterPath, FetchPosters.MEDIUM_IMAGE_SIZE);

            // Fetch the Movie Videos & Reviews - Populates the correspondent UI.
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(FETCH_VIDEOS_LOADER_ID, movieBundle, this);
            loaderManager.initLoader(FETCH_REVIEWS_LOADER_ID, movieBundle, this);

            this.setTitle(movieBundle.getString(JsonUtils.MOVIE_ORIGINAL_TITLE));

            String stringReleaseDate = "(" + movieBundle.getString(JsonUtils.MOVIE_RELEASE_DATE) + ")";
            mActivityDetailBinding.detailReleaseDate.setText(stringReleaseDate);

            String stringRate = String.valueOf(movieBundle.getDouble(JsonUtils.MOVIE_VOTE_AVERAGE));
            mActivityDetailBinding.detailRate.setText(stringRate);

            mActivityDetailBinding.detailOverview.setText(movieBundle.getString(JsonUtils.MOVIE_OVERVIEW));

            // Re-Adjusts the ListViews Height.
            if (!loaderManager.hasRunningLoaders()) {
                loaderManager.getLoader(FETCH_VIDEOS_LOADER_ID).forceLoad();
                loaderManager.getLoader(FETCH_REVIEWS_LOADER_ID).forceLoad();
            }
        }
    }

    // Helper Method - Finishes the detail activity.
    private void closeActivityOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    // Instantiates a Loader for a given ID.
    @NonNull
    @Override
    public Loader onCreateLoader(int loaderID, @Nullable Bundle bundle) {
        assert bundle != null;
        int movieId = bundle.getInt(JsonUtils.MOVIE_ID);
        switch (loaderID) {
            case FETCH_VIDEOS_LOADER_ID:
                return new FetchVideos(this, movieId);
            case FETCH_REVIEWS_LOADER_ID:
                return new FetchReviews(this, movieId);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderID);
        }
    }

    // Updates the UI with the Loaders Results after Network has Completed.
    @Override
    public void onLoadFinished(@NonNull Loader loader, Object loadResults) {
        int loaderId = loader.getId();
        switch (loaderId) {
            case FETCH_VIDEOS_LOADER_ID:
                displayVideos(loadResults);
                return;
            case FETCH_REVIEWS_LOADER_ID:
                displayReviews(loadResults);
                return;
            default:
                throw new RuntimeException("Loader Not Implemented: " + loader);
        }
    }

    // Resets a Loader for a given ID - Clears any References to Loader's Data.
    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        int loaderId = loader.getId();
        switch (loaderId) {
            case FETCH_VIDEOS_LOADER_ID:
                mVideosAdapter.setmVideos(null);
                return;
            case FETCH_REVIEWS_LOADER_ID:
                mReviewsAdapter.setmReviews(null);
                return;
            default:
                throw new RuntimeException("Loader Not Implemented: " + loader);
        }
    }

    // Helper Method - Displays the list of Videos.
    private void displayVideos(Object loadResults) {
        if (loadResults != null && loadResults instanceof String[]) {
            String[] videos = (String[]) loadResults;
            mVideosAdapter.setmVideos(videos);

            LayoutParams params = adjustListViewHeight(mActivityDetailBinding.detailVideos, mVideosAdapter);
            mActivityDetailBinding.detailVideos.setLayoutParams(params);
            mActivityDetailBinding.detailVideos.requestLayout();
        }
    }

    // Helper Method - Displays the list of Reviews.
    private void displayReviews(Object loadResults) {
        if (loadResults != null && loadResults instanceof ContentValues[]) {
            ContentValues[] reviews = (ContentValues[]) loadResults;
            mReviewsAdapter.setmReviews(reviews);

            LayoutParams params = adjustListViewHeight(mActivityDetailBinding.detailReviews, mReviewsAdapter);
            mActivityDetailBinding.detailReviews.setLayoutParams(params);
            mActivityDetailBinding.detailReviews.requestLayout();
        }
    }

    // Helper Method - Adjusts the ListView Height.
    private LayoutParams adjustListViewHeight(ListView listView, ArrayAdapter adapter) {
        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);

        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        return params;
    }


    // Helper Method - Adds/Deletes a Movie from Favorites.
    public void addToFavorite(View view) {
        // check favorite database 1st ...
        // enable/disable the button ...
        // change button's text ...
        // add/delete from database ...
    }
}
