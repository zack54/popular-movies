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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.FetchDataTask;
import com.example.android.popularmovies.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays a grid of Movie Posters.
 * Implements RecyclerViewAdapter.OnClickListener - so it can handle RecyclerView items Clicks.
 * Implements FetchDataTask.OnTaskCompleteListener - so it can be invoked after AsyncTask Completed.
 */
public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnClickListener, FetchDataTask.OnTaskCompleteListener<Movie[]> {

    private static final String CRITERIA_KEY = "criteria";

    @BindView(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    // Member Variables - Used to make UX better.
    @BindView(R.id.tv_connection_error_message)
    private TextView mInternetConnectionErrorMessage;
    @BindView(R.id.tv_loading_error_message)
    private TextView mLoadingErrorMessage;
    @BindView(R.id.pb_loading_indicator)
    private ProgressBar mLoadingIndicator;

    // Member Variable - Saves the activity's state by Storing the Current Sort Criteria.
    private String currentSortCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connects member variables to views in the main activity layout.
        ButterKnife.bind(this);

        // Setups the RecyclerView.
        GridLayoutManager gridLayoutManager = null;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this, 3);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this, 2);
        }
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerViewAdapter = new RecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        // Restores the state of main activity on orientation.
        if (savedInstanceState != null) {
            currentSortCriteria = savedInstanceState.getString(CRITERIA_KEY);
        } else {
            currentSortCriteria = NetworkUtils.getPopularSortCriteria();
        }

        // Load the Data.
        loadData(currentSortCriteria);
    }

    // Helper Method - Starts Background Task based on Sort Criteria if there is Internet connection
    private void loadData(String sortCriteria) {
        if (connectedToInternet()) {
            mRecyclerViewAdapter.setmMovies(null);
            showData();
            currentSortCriteria = sortCriteria;
            new FetchDataTask(this).execute(currentSortCriteria);
        } else {
            showInternetConnectionErrorMessage();
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
    private void showInternetConnectionErrorMessage() {
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
                loadData(NetworkUtils.getPopularSortCriteria());
                return true;
            case R.id.action_top_rated:
                loadData(NetworkUtils.getTopRatedSortCriteria());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Saves the state of main activity before orientation.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CRITERIA_KEY, currentSortCriteria);
        super.onSaveInstanceState(outState);
    }

    // Prepares the UI Before Network Starts.
    @Override
    public void onTaskStart() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    // Updates the UI with the Result after Network has Completed.
    @Override
    public void onTaskComplete(Movie[] movies) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movies != null) {
            showData();
            mRecyclerViewAdapter.setmMovies(movies);
        } else {
            showLoadingErrorMessage();
        }
    }
}
