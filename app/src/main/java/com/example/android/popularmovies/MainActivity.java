package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
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
import com.example.android.popularmovies.utilities.JsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private String currentCriteria;

    private TextView mLoadingErrorMessage;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingErrorMessage = findViewById(R.id.tv_loading_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRecyclerView = findViewById(R.id.recyclerview);

        // Setup the RecyclerView.
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

        // Load the Data.
        loadData(NetworkUtils.getPopularSortCriteria());
    }

    /**
     * This is a Helper Method
     * Used to Start a Background Task & Get Data in the background.
     */
    private void loadData(String sortCriteria) {
        if (!sortCriteria.equals(currentCriteria) || (mRecyclerViewAdapter.getmMovies() == null)) {
            mRecyclerViewAdapter.setmMovies(null);
            showData();
            currentCriteria = sortCriteria;
            new FetchTask().execute(currentCriteria);
        }

    }

    private void showData() {
        mLoadingErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie currentMovie) {
        launchDetailActivity(currentMovie);
    }

    private void launchDetailActivity(Movie currentMovie) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra("movie", currentMovie);
        startActivity(detailIntent);
    }

    /**
     * A Background Task to fetch Data from the Internet.
     */
    @SuppressLint("StaticFieldLeak")
    public class FetchTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... strings) {

            /* If there's no zip code, there's nothing to look up. */
            if (strings.length == 0) {
                return null;
            }

            String sortCriteria = strings[0];
            URL url = NetworkUtils.buildUrl(sortCriteria);

            try {
                String jsonString = NetworkUtils.getResponseFromHttpUrl(url);
                return JsonUtils.parseJson(jsonString);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showData();
                mRecyclerViewAdapter.setmMovies(movies);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

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
}
