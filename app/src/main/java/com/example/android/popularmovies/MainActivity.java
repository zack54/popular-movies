package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.JsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    private String currentCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerViewAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        // Load the Data.
        loadData(NetworkUtils.getPopularSortCriteria());
    }

    /**
     * This is a Helper Method
     * Used to Start a Background Task & Get Data in the background.
     */
    private void loadData(String sortCriteria) {
        if (!sortCriteria.equals(currentCriteria)) {
            new FetchTask().execute(sortCriteria);
        }

    }

    /**
     * A Background Task to fetch Data from the Internet.
     */
    public class FetchTask extends AsyncTask<String, Void, Movie[]> {

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
            if (movies != null) {
                mRecyclerViewAdapter.setmMovies(movies);
            } else {
                super.onPostExecute(null);
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
