package com.example.android.popularmovies;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private String[] mPosters = {"aaa", "bbb", "ccc"};
    // private String[] mPosters;

    public RecyclerViewAdapter() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_poster);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String poster = mPosters[position];
        Uri uri = Uri.parse(poster);
        // holder.mImageView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return mPosters.length;
    }

    public void setmPosters(String[] mPosters) {
        this.mPosters = mPosters;
        notifyDataSetChanged();
    }
}
