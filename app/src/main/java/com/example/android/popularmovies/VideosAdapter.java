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
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Exposes a list of Videos - Plays Trailers.
 */
class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    // Member Variable - Stores the List of Videos.
    private String[] mVideos;
    private final Context mContext;

    // Constructor - Initializes the List of Videos.
    VideosAdapter(Context context) {
        mContext = context;
    }

    // Sets the List of Videos & Notifies the Adapter that Data has changed.
    public void setmVideos(String[] videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    // Initializes a ViewHolder using the Item's "XML" Layout.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.detail_video_item, parent, false);
        return new ViewHolder(itemView);
    }

    // Populates & Binds the correct Video from Database/Cloud based on position & Sort Criteria.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView textView = holder.trailerTextView;
        String trailerNumber = mContext.getString(R.string.detail_trailer_label) + " " + String.valueOf(position + 1);
        textView.setText(trailerNumber);
    }

    // Returns the Total Number of Videos.
    @Override
    public int getItemCount() {
        if (mVideos != null) return mVideos.length;
        return 0;
    }

    // Caches Views for Video item to be reused when needed.
    class ViewHolder extends RecyclerView.ViewHolder {

        // Holds references to Sub-View within a List Item View.
        final TextView trailerTextView;
        final Button shareButton;

        ViewHolder(View itemView) {
            super(itemView);
            trailerTextView = itemView.findViewById(R.id.detail_video_number);
            shareButton = itemView.findViewById(R.id.detail_share_button);

            trailerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String videoId = mVideos[getAdapterPosition()];
                    Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                    youtubeIntent.putExtra("VIDEO_ID", videoId);
                    mContext.startActivity(youtubeIntent);
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String videoId = mVideos[getAdapterPosition()];
                    Uri uri = new Uri.Builder()
                            .scheme("https")
                            .authority("www.youtube.com")
                            .appendPath("watch")
                            .appendQueryParameter("v", videoId)
                            .build();

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                            mContext.getString(R.string.detail_share_url_message_subject));
                    shareIntent.putExtra(Intent.EXTRA_TEXT, uri.toString());
                    mContext.startActivity(Intent.createChooser(shareIntent,
                            mContext.getString(R.string.detail_share_url_chooser_title)));
                }
            });
        }
    }
}
