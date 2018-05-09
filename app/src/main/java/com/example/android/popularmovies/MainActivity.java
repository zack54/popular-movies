package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

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
        loadData();
    }

    /**
     * This is a Helper Method
     * Used to Start a Background Task & Get Data in the background.
     */
    private void loadData() {
        // TODO: Start a background task
    }

}
