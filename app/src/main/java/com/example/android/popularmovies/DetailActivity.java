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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.popularmovies.data.FavoriteMoviesContract.Reviews;
import com.example.android.popularmovies.data.FavoriteMoviesContract.Videos;
import com.example.android.popularmovies.data.FavoriteMoviesContract.Movies;
import com.example.android.popularmovies.databinding.ActivityDetailBinding;
import com.example.android.popularmovies.utilities.BitmapUtility;
import com.example.android.popularmovies.utilities.FetchPoster;
import com.example.android.popularmovies.utilities.FetchReviews;
import com.example.android.popularmovies.utilities.FetchVideos;
import com.example.android.popularmovies.utilities.JsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

/**
 * Displays details about each Movie.
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final int FETCH_VIDEOS_LOADER_ID = 10;
    private static final int FETCH_REVIEWS_LOADER_ID = 20;

    private static final String MOVIE_BUNDLE_KEY = "movie_bundle_key";
    private static final String FAVORITE_MOVIE_KEY = "favorite_key";
    private static final String MOVIE_IMAGE_BYTES = "movie_image_bytes";
    private static final String VIDEOS_KEY = "videos_key";

    // Member Variable - Holds references to the Views in Detail Activity Layout.
    private ActivityDetailBinding mActivityDetailBinding;
    private ContentResolver mContentResolver;
    private VideosAdapter mVideosAdapter;
    private ReviewsAdapter mReviewsAdapter;

    // Member Variables - Saves the Activity's state for Runtime Configuration changes.
    private Bundle mCurrentMovie;
    private String mCurrentSortCriteria;
    private boolean mMovieIsFavorite;
    private byte[] mCurrentMovieImageBytes;
    private String mCurrentMovieId;
    private Uri mCurrentMovieUri;

    private String[] mCurrentVideos;
    private ContentValues[] mCurrentReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Data Binding - Links Views in the Detail Activity Layout UI.
        mActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mContentResolver = getContentResolver();

        // Setups the ListViews.
        setupListViews(this);

        // Adds Up Navigation Button into the Action Bar.
        addUpNavigationButton();

        // Restore the Activity's State
        restoreInstanceState(savedInstanceState);

        // Populates the Movie's Basic Data.
        loadBasicData();

        // Populates the Movie's Extra Data.
        loadMoreData();
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


    // Helper Method - Restores Current SortCriteria on Orientation or Set to Default SortCriteria.
    private void restoreInstanceState(Bundle savedInstanceState) {
        Intent intent = getIntent();

        if (savedInstanceState != null) {
            mCurrentMovie = savedInstanceState.getBundle(MOVIE_BUNDLE_KEY);
            mCurrentSortCriteria = savedInstanceState.getString(NetworkUtils.CRITERIA_KEY);
            mMovieIsFavorite = savedInstanceState.getBoolean(FAVORITE_MOVIE_KEY);
            mCurrentMovieImageBytes = savedInstanceState.getByteArray(MOVIE_IMAGE_BYTES);
            mCurrentVideos = savedInstanceState.getStringArray(VIDEOS_KEY);
        } else if (intent == null) {
            closeActivityOnError();
        } else {
            mCurrentMovie = intent.getExtras();
            mCurrentSortCriteria = intent.getStringExtra(NetworkUtils.CRITERIA_KEY);
            if (mCurrentSortCriteria.equals(NetworkUtils.FAVORITE_CRITERIA)) {
                mMovieIsFavorite = true;
                mCurrentMovieImageBytes = mCurrentMovie.getByteArray(JsonUtils.MOVIE_POSTER);
            }
        }

        if (mCurrentMovie == null) {
            closeActivityOnError();
        } else {
            mCurrentMovieId = String.valueOf(mCurrentMovie.getInt(JsonUtils.MOVIE_ID));
            mCurrentMovieUri = Movies.CONTENT_URI.buildUpon()
                    .appendPath(mCurrentMovieId)
                    .build();
        }
    }

    // Saves the Current Sort Criteria before Orientation.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBundle(MOVIE_BUNDLE_KEY, mCurrentMovie);
        outState.putString(NetworkUtils.CRITERIA_KEY, mCurrentSortCriteria);
        outState.putBoolean(FAVORITE_MOVIE_KEY, mMovieIsFavorite);
        outState.putByteArray(MOVIE_IMAGE_BYTES, mCurrentMovieImageBytes);
        outState.putStringArray(VIDEOS_KEY, mCurrentVideos);
        super.onSaveInstanceState(outState);
    }

    // Helper Method - Finishes the detail activity.
    private void closeActivityOnError() {
        finish();
        Toast.makeText(this, R.string.detail_open_error_message, Toast.LENGTH_SHORT).show();
    }


    // Helper Method - Populates the Movie's Basic Data (Title, Release Date, Vote Average, Overview)
    private void loadBasicData() {
        this.setTitle(mCurrentMovie.getString(JsonUtils.MOVIE_ORIGINAL_TITLE));

        String stringReleaseDate = "(" + mCurrentMovie.getString(JsonUtils.MOVIE_RELEASE_DATE) + ")";
        mActivityDetailBinding.detailReleaseDate.setText(stringReleaseDate);

        String stringRate = String.valueOf(mCurrentMovie.getDouble(JsonUtils.MOVIE_VOTE_AVERAGE));
        mActivityDetailBinding.detailRate.setText(stringRate);

        mActivityDetailBinding.detailOverview.setText(mCurrentMovie.getString(JsonUtils.MOVIE_OVERVIEW));
    }

    // Helper Method - Populates the Movie's Extra Data (Poster, Video Trailers, Reviews)
    private void loadMoreData() {

        // Checks is the Current Movie is a Favorite movie.
        if (mMovieIsFavorite || movieIsInMoviesTable()) {

            // Switch the Button to Delete.
            setButtonToDelete();

            // Sets the Movie's Poster.
            Bitmap imageBitmap = BitmapUtility.getImage(mCurrentMovieImageBytes);
            mActivityDetailBinding.detailPoster.setImageBitmap(imageBitmap);

            // Sets the Movie's Trailers.
            String[] videos = getTrailersFromVideosTable();
            displayVideos(videos);

            // Sets the Movies's Reviews.
            ContentValues[] reviews = getReviewsFromReviewsTable();
            displayReviews(reviews);

            // When Current Movie is Not in Database.
        } else {

            // Switch the Button to Add.
            setButtonToAdd();

            // Fetch the Movie's Poster from the Cloud.
            ImageView posterImageView = mActivityDetailBinding.detailPoster;
            String posterRelativePath = mCurrentMovie.getString(JsonUtils.MOVIE_POSTER_PATH);
            Bitmap movieBitmap = FetchPoster.intoImageViewAndReturnBitmap(
                    posterImageView,
                    posterRelativePath,
                    FetchPoster.MEDIUM_IMAGE_SIZE);
            mCurrentMovieImageBytes = BitmapUtility.getBytes(movieBitmap);

            // Fetch the Movie's Videos & Reviews from the Cloud.
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(FETCH_VIDEOS_LOADER_ID, mCurrentMovie, this);
            loaderManager.restartLoader(FETCH_REVIEWS_LOADER_ID, mCurrentMovie, this);

//            // Re-Adjusts the ListViews Height on Screen Rotation.
//            if (!loaderManager.hasRunningLoaders()) {
//                loaderManager.getLoader(FETCH_VIDEOS_LOADER_ID).forceLoad();
//                loaderManager.getLoader(FETCH_REVIEWS_LOADER_ID).forceLoad();
//            }
        }
    }

    // TODO: find out how to move it to the background thread not to slow main thread ....
    // Helper Method - Query the Movies Table to Check if Movie is Favorite, if so Store its Poster.
    private boolean movieIsInMoviesTable() {
        String[] selectionArgs = {mCurrentMovieId};
        Cursor cursor = mContentResolver.query(
                mCurrentMovieUri,
                null,
                null,
                selectionArgs,
                null);

        if (cursor != null) {
            if (cursor.getCount() != 0) {

                // Get image from the Database - Sets mMovieIsFavorite & mCurrentMovieImageBytes.
                if (cursor.moveToFirst()) {
                    mCurrentMovieImageBytes = cursor.getBlob(cursor.getColumnIndex(Movies.COLUMN_POSTER));
                }
                mMovieIsFavorite = true;
            }
            cursor.close();
        }
        return mMovieIsFavorite;
    }

    // Helper Method - Query the Videos Table to Get the correct Trailers & Update local reference.
    private String[] getTrailersFromVideosTable() {
        String[] videos = new String[0];

        String[] selectionArgs = {mCurrentMovieId};
        Cursor cursor = mContentResolver.query(
                Videos.CONTENT_URI,
                null,
                null,
                selectionArgs,
                null);

        if (cursor != null) {
            videos = new String[cursor.getCount()];
            if (cursor.moveToFirst()) {
                do {
                    videos[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex(Videos.COLUMN_VIDEO_PATH));
                } while (cursor.moveToNext());
            }
            mCurrentVideos = videos;
            cursor.close();
        }
        return videos;
    }

    // Helper Method - Query the Reviews Table to Get the correct Reviews & Update local reference.
    private ContentValues[] getReviewsFromReviewsTable() {
        ContentValues[] reviews = new ContentValues[0];

        String[] selectionArgs = {mCurrentMovieId};
        Cursor cursor = mContentResolver.query(
                Reviews.CONTENT_URI,
                null,
                null,
                selectionArgs,
                null);

        if (cursor != null) {
            reviews = new ContentValues[cursor.getCount()];
            if (cursor.moveToFirst()) {
                do {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(JsonUtils.REVIEW_AUTHOR, cursor.getString(cursor.getColumnIndex(Reviews.COLUMN_REVIEW_AUTHOR)));
                    contentValues.put(JsonUtils.REVIEW_CONTENT, cursor.getString(cursor.getColumnIndex(Reviews.COLUMN_REVIEW_CONTENT)));
                    reviews[cursor.getPosition()] = contentValues;
                } while (cursor.moveToNext());
            }
            mCurrentReviews = reviews;
            cursor.close();
        }
        return reviews;
    }


    // Helper Method - Updates the Button's Text & Color at Runtime.
    private void setButtonToDelete() {
        mActivityDetailBinding.detailButton.setText(getResources().getString(R.string.detail_delete_from_favorite));
        mActivityDetailBinding.detailButton.setBackgroundColor(getResources().getColor(R.color.color_delete));
    }

    // Helper Method - Updates the Button's Text & Color at Runtime.
    private void setButtonToAdd() {
        mActivityDetailBinding.detailButton.setText(getResources().getString(R.string.detail_add_to_favorite));
        mActivityDetailBinding.detailButton.setBackgroundColor(getResources().getColor(R.color.color_add));
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
                if (loadResults != null && loadResults instanceof String[]) {
                    String[] videos = (String[]) loadResults;
                    mCurrentVideos = videos;
                    displayVideos(videos);
                }
                return;
            case FETCH_REVIEWS_LOADER_ID:
                if (loadResults != null && loadResults instanceof ContentValues[]) {
                    ContentValues[] reviews = (ContentValues[]) loadResults;
                    mCurrentReviews = reviews;
                    displayReviews(reviews);
                }
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
                mCurrentVideos = null;
                mVideosAdapter.setmVideos(null);
                return;
            case FETCH_REVIEWS_LOADER_ID:
                // ...
                mReviewsAdapter.setmReviews(null);
                return;
            default:
                throw new RuntimeException("Loader Not Implemented: " + loader);
        }
    }

    // Helper Method - Displays the list of Videos.
    private void displayVideos(String[] videos) {
        mVideosAdapter.setmVideos(videos);

        LayoutParams params = adjustListViewHeight(mActivityDetailBinding.detailVideos, mVideosAdapter);
        mActivityDetailBinding.detailVideos.setLayoutParams(params);
        mActivityDetailBinding.detailVideos.requestLayout();
    }

    // Helper Method - Displays the list of Reviews.
    private void displayReviews(ContentValues[] reviews) {
        mReviewsAdapter.setmReviews(reviews);

        LayoutParams params = adjustListViewHeight(mActivityDetailBinding.detailReviews, mReviewsAdapter);
        mActivityDetailBinding.detailReviews.setLayoutParams(params);
        mActivityDetailBinding.detailReviews.requestLayout();
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



    // Handles Favorite Button Click events.
    public void favoriteButtonClickListener(View view) {
        if (mMovieIsFavorite) {
            deleteMovieFromFavorite();
        } else {
            addMovieToFavorite();
        }
    }

    // Helper Method - Deletes a Movie from the Favorite Database.
    private void deleteMovieFromFavorite() {
        String[] selectionArgs = {mCurrentMovieId};

        int rowsDeleted = mContentResolver.delete(mCurrentMovieUri, null, selectionArgs);
        mContentResolver.delete(Videos.CONTENT_URI, null, selectionArgs);
        mContentResolver.delete(Reviews.CONTENT_URI, null, selectionArgs);

        if (rowsDeleted != 0) {
            Toast.makeText(this, "Movie Deleted From Favorite Successfully", Toast.LENGTH_SHORT).show();
            mMovieIsFavorite = false;
            setButtonToAdd();
        } else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper Method - Inserts a Movie from into the Favorite Database.
    private void addMovieToFavorite() {

        // Add a Movie to Movies Table.
        ContentValues movieValues = new ContentValues();
        movieValues.put(Movies.COLUMN_ID, mCurrentMovie.getInt(JsonUtils.MOVIE_ID));
        movieValues.put(Movies.COLUMN_VOTE_AVERAGE, mCurrentMovie.getDouble(JsonUtils.MOVIE_VOTE_AVERAGE));
        movieValues.put(Movies.COLUMN_POSTER, mCurrentMovieImageBytes);
        movieValues.put(Movies.COLUMN_ORIGINAL_TITLE, mCurrentMovie.getString(JsonUtils.MOVIE_ORIGINAL_TITLE));
        movieValues.put(Movies.COLUMN_OVERVIEW, mCurrentMovie.getString(JsonUtils.MOVIE_OVERVIEW));
        movieValues.put(Movies.COLUMN_RELEASE_DATE, mCurrentMovie.getString(JsonUtils.MOVIE_RELEASE_DATE));

        Uri movieReturnedUri = mContentResolver.insert(Movies.CONTENT_URI, movieValues);

        // Add the Movie's Trailers to Videos Table.
        for (String video : mCurrentVideos) {
            ContentValues videoValue = new ContentValues();
            videoValue.put(Videos.COLUMN_VIDEO_PATH, video);
            videoValue.put(Videos.COLUMN_MOVIE_ID, Integer.valueOf(mCurrentMovieId));

            Uri videoReturnedUri = mContentResolver.insert(Videos.CONTENT_URI, videoValue);
        }

        // Add the Movie's Reviews to Reviews Table.
        for (ContentValues review : mCurrentReviews) {
            ContentValues reviewValue = new ContentValues();
            reviewValue.put(Reviews.COLUMN_REVIEW_AUTHOR, review.getAsString(JsonUtils.REVIEW_AUTHOR));
            reviewValue.put(Reviews.COLUMN_REVIEW_CONTENT, review.getAsString(JsonUtils.REVIEW_CONTENT));
            reviewValue.put(Reviews.COLUMN_MOVIE_ID, Integer.valueOf(mCurrentMovieId));

            Uri reviewReturnedUri = mContentResolver.insert(Reviews.CONTENT_URI, reviewValue);
        }

        if (movieReturnedUri != null) {
            Toast.makeText(this, "Movie Added to Favorites Successfully", Toast.LENGTH_SHORT).show();
            mMovieIsFavorite = true;
            setButtonToDelete();
        } else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
