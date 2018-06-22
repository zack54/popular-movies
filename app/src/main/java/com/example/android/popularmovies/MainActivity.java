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

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.utilities.FetchMovies;
import com.example.android.popularmovies.utilities.NetworkUtils;

/**
 * Displays a grid of Movie Posters.
 * Implements MoviesAdapter.OnClickListener - so it can handle RecyclerView items Clicks.
 * Implements LoaderManager.LoaderCallbacks<Bundle[]> - so it can be invoked after Loader Completed
 */
public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnClickListener,
        LoaderManager.LoaderCallbacks<Bundle[]> {

    private static final int FETCH_MOVIES_LOADER_ID = 0;
    private static final String INITIALIZE_LOADING = "initialize";
    private static final String RESTART_LOADING = "restart";

    // Member Variable - Holds references to the Views in Main Activity Layout.
    private ActivityMainBinding mActivityMainBinding;
    private MoviesAdapter mMoviesAdapter;

    // Member Variable - Saves the activity's state by Storing the Current Sort Criteria.
    private String mCurrentSortCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Data Binding - Links Views in the Detail Activity Layout UI.
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Setups the RecyclerView.
        setupRecyclerView();

        // Restore the Activity's State
        restoreInstanceState(savedInstanceState);

        // Sets the Activity Title to Current Sort Criteria.
        setActivityTitle();

        // Load the Data.
        loadData(mCurrentSortCriteria, INITIALIZE_LOADING);
    }


    // Helper Method - Setups the RecyclerView's LayoutManager based on current Orientation.
    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = null;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this, 3);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this, 2);
        }
        mActivityMainBinding.mainRecyclerView.setLayoutManager(gridLayoutManager);
        mActivityMainBinding.mainRecyclerView.setHasFixedSize(true);
        mMoviesAdapter = new MoviesAdapter(this);
        mActivityMainBinding.mainRecyclerView.setAdapter(mMoviesAdapter);
    }

    // Helper Method - Restores Current SortCriteria on Orientation and back from Detail Activity.
    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentSortCriteria = savedInstanceState.getString(NetworkUtils.CRITERIA_KEY);
        } else {
            mCurrentSortCriteria = NetworkUtils.getDefaultSortCriteria();
        }
    }

    // Saves the Current Sort Criteria before Orientation.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(NetworkUtils.CRITERIA_KEY, mCurrentSortCriteria);
        super.onSaveInstanceState(outState);
    }



    // Helper Method - Sets the Activity Tiles.
    private void setActivityTitle() {
        switch (mCurrentSortCriteria) {
            case NetworkUtils.POPULAR_SORT_CRITERIA:
                this.setTitle(R.string.main_most_popular_option_menu);
                break;
            case NetworkUtils.TOP_RATED_SORT_CRITERIA:
                this.setTitle(R.string.main_top_rated_option_menu);
                break;
            case NetworkUtils.FAVORITE_CRITERIA:
                this.setTitle(R.string.main_favorite_option_menu);
                break;
        }
    }


    // Helper Method - Starts Background Task to Load Data from Database/Cloud based on Sort Criteria.
    private void loadData(String sortCriteria, String flag) {

        // Update the Current Sort Criteria.
        mCurrentSortCriteria = sortCriteria;

        // Prepares the UI Before Network Starts.
        onLoadStarted();

        // Prepares SortCriteria to pass to the Loader.
        Bundle bundle = new Bundle();
        bundle.putString(NetworkUtils.CRITERIA_KEY, sortCriteria);

        switch (sortCriteria) {
            case NetworkUtils.FAVORITE_CRITERIA:
                startLoader(bundle, flag);
                return;
            case NetworkUtils.POPULAR_SORT_CRITERIA:
            case NetworkUtils.TOP_RATED_SORT_CRITERIA:
                if (connectedToInternet()) {
                    startLoader(bundle, flag);
                } else {
                    mActivityMainBinding.mainLoadingIndicator.setVisibility(View.INVISIBLE);
                    showConnectionErrorMessage();
                }
        }
    }

    // Helper Method - Initialize/Restart the Loader based on the flag.
    private void startLoader(Bundle bundle, String flag) {
        switch (flag) {
            case INITIALIZE_LOADING:
                getSupportLoaderManager().initLoader(FETCH_MOVIES_LOADER_ID, bundle,
                        this);
                break;
            case RESTART_LOADING:
                getSupportLoaderManager().restartLoader(FETCH_MOVIES_LOADER_ID, bundle,
                        this);
                break;
        }
    }

    // Helper Method - Checks Internet Connection so the device can save one unneeded network call.
    private boolean connectedToInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return ((networkInfo != null) && (networkInfo.isConnectedOrConnecting()));
    }


    // Prepares the UI Before Network Starts.
    public void onLoadStarted() {
        mMoviesAdapter.setmMovies(null, mCurrentSortCriteria);
        showData();
        mActivityMainBinding.mainLoadingIndicator.setVisibility(View.VISIBLE);
    }

    // Instantiates a New Loader for given ID.
    @NonNull
    @Override
    public Loader<Bundle[]> onCreateLoader(int loaderID, @Nullable Bundle bundle) {
        assert bundle != null;
        return new FetchMovies(this, bundle.getString(NetworkUtils.CRITERIA_KEY));
    }

    // Updates the UI with the Loader Results after Network has Completed.
    @Override
    public void onLoadFinished(@NonNull Loader<Bundle[]> loader, Bundle[] data) {
        mActivityMainBinding.mainLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            showData();
            mMoviesAdapter.setmMovies(data, mCurrentSortCriteria);
            if (data.length == 0 && mCurrentSortCriteria.equals(NetworkUtils.FAVORITE_CRITERIA)) {
                Toast.makeText(this, R.string.main_empty_favorite_movies, Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            showLoadingErrorMessage();
        }
    }

    // Resets the Loader - Clears any References to Loader's Data.
    @Override
    public void onLoaderReset(@NonNull Loader<Bundle[]> loader) {
        mMoviesAdapter.setmMovies(null, mCurrentSortCriteria);
    }


    // Helper Method - Makes the Movies Data visible & Hides the Error Messages.
    private void showData() {
        mActivityMainBinding.mainConnectionErrorMessage.setVisibility(View.INVISIBLE);
        mActivityMainBinding.mainLoadingErrorMessage.setVisibility(View.INVISIBLE);

        mActivityMainBinding.mainRecyclerView.setVisibility(View.VISIBLE);
    }

    // Helper Method - Makes Loading Error Message visible & Hides Movies Data and Connection Error.
    private void showLoadingErrorMessage() {
        mActivityMainBinding.mainConnectionErrorMessage.setVisibility(View.INVISIBLE);
        mActivityMainBinding.mainRecyclerView.setVisibility(View.INVISIBLE);

        mActivityMainBinding.mainLoadingErrorMessage.setVisibility(View.VISIBLE);
    }

    // Helper Method - Makes Connection Error Message visible & Hides Movies Data and Loading Error.
    private void showConnectionErrorMessage() {
        mActivityMainBinding.mainRecyclerView.setVisibility(View.INVISIBLE);
        mActivityMainBinding.mainLoadingErrorMessage.setVisibility(View.INVISIBLE);

        mActivityMainBinding.mainConnectionErrorMessage.setVisibility(View.VISIBLE);
    }


    // Handles RecyclerView items Clicks - Launches the detail activity for the correct Movie.
    @Override
    public void onClick(Bundle currentMovie) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtras(currentMovie);
        detailIntent.putExtra(NetworkUtils.CRITERIA_KEY, mCurrentSortCriteria);
        startActivity(detailIntent);
    }


    // Adds an Options Menu to Main Activity.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    // Handles Options Menu Items Clicks - Load Data with correct Sort Criteria.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_popular:
                loadData(NetworkUtils.POPULAR_SORT_CRITERIA, RESTART_LOADING);
                break;
            case R.id.action_top_rated:
                loadData(NetworkUtils.TOP_RATED_SORT_CRITERIA, RESTART_LOADING);
                break;
            case R.id.action_favorite:
                loadData(NetworkUtils.FAVORITE_CRITERIA, RESTART_LOADING);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        setActivityTitle();
        return true;
    }


    // Refresh the Main Activity to reflect Database Update.
    @Override
    protected void onResume() {
        super.onResume();
        loadData(mCurrentSortCriteria, RESTART_LOADING);
    }
}
