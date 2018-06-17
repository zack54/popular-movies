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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.FetchMovies;
import com.example.android.popularmovies.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays a grid of Movie Posters.
 * Implements MoviesAdapter.OnClickListener - so it can handle RecyclerView items Clicks.
 * Implements FetchDataAsyncTask.OnFetchDataTaskListener - so it can be invoked after AsyncTask Completed
 */
public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnClickListener,
        LoaderManager.LoaderCallbacks<Movie[]> {

    private static final int FETCH_MOVIES_LOADER_ID = 0;
    private static final String INITIALIZE_LOADING = "initialize";
    private static final String RESTART_LOADING = "restart";

    // Member Variables - Used to make UX better.
    @BindView(R.id.tv_connection_error_message)
    TextView mInternetConnectionErrorMessage;
    @BindView(R.id.tv_loading_error_message)
    TextView mLoadingErrorMessage;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    // Member Variable - Saves the activity's state by Storing the Current Sort Criteria.
    private String mCurrentSortCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connects member variables to views in the main activity layout.
        ButterKnife.bind(this);

        // Setups the RecyclerView.
        setupRecyclerView();

        // Restore the Activity's State
        restoreInstanceState(savedInstanceState);

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
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);
    }


    // Helper Method - Restores the Main Activity State on orientation or get Default SortCriteria.
    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentSortCriteria = savedInstanceState.getString(NetworkUtils.CRITERIA_KEY);
        } else {
            mCurrentSortCriteria = NetworkUtils.getDefaultSortCriteria();
        }
    }

    // Saves the state of main activity before orientation.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(NetworkUtils.CRITERIA_KEY, mCurrentSortCriteria);
        super.onSaveInstanceState(outState);
    }



    // Helper Method - Starts Background Task based on Sort Criteria if there is Internet connection
    private void loadData(String sortCriteria, String flag) {
        if (connectedToInternet()) {

            // Prepares the UI Before Network Starts.
            onLoadStarted();

            Bundle bundle = new Bundle();
            bundle.putString(NetworkUtils.CRITERIA_KEY, sortCriteria);

            switch (flag) {
                case INITIALIZE_LOADING:
                    getSupportLoaderManager().initLoader(FETCH_MOVIES_LOADER_ID, bundle, this);
                    break;
                case RESTART_LOADING:
                    getSupportLoaderManager().restartLoader(FETCH_MOVIES_LOADER_ID, bundle, this);
                    break;
            }

            mCurrentSortCriteria = sortCriteria;
        } else {
            showConnectionErrorMessage();
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
        mMoviesAdapter.setmMovies(null);
        showData();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    // Instantiates a New Loader for given ID.
    @NonNull
    @Override
    public Loader<Movie[]> onCreateLoader(int loaderID, @Nullable Bundle args) {
        assert args != null;
        return new FetchMovies(this, args.getString(NetworkUtils.CRITERIA_KEY));
    }

    // Updates the UI with the Loader Results after Network has Completed.
    @Override
    public void onLoadFinished(@NonNull Loader<Movie[]> loader, Movie[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            showData();
            mMoviesAdapter.setmMovies(data);
        } else {
            showLoadingErrorMessage();
        }
    }

    // Resets the Loader - Clears any References to Loader's Data.
    @Override
    public void onLoaderReset(@NonNull Loader<Movie[]> loader) {
        mMoviesAdapter.setmMovies(null);
    }



    // Helper Method - Makes the Movies Data visible & Hides the Error Messages.
    private void showData() {
        mInternetConnectionErrorMessage.setVisibility(View.INVISIBLE);
        mLoadingErrorMessage.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    // Helper Method - Makes Loading Error Message visible & Hides Movies Data and Connection Error.
    private void showLoadingErrorMessage() {
        mInternetConnectionErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mLoadingErrorMessage.setVisibility(View.VISIBLE);
    }

    // Helper Method - Makes Connection Error Message visible & Hides Movies Data and Loading Error.
    private void showConnectionErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingErrorMessage.setVisibility(View.INVISIBLE);

        mInternetConnectionErrorMessage.setVisibility(View.VISIBLE);
    }


    // Handles RecyclerView items Clicks - Launches the detail activity for the correct Movie.
    @Override
    public void onClick(Movie currentMovie) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra("movie", currentMovie);
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
                return true;
            case R.id.action_top_rated:
                loadData(NetworkUtils.TOP_RATED_SORT_CRITERIA, RESTART_LOADING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
