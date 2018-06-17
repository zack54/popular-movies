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
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Exposes a list of Videos - Plays Trailers.
 */
public class VideosAdapter extends ArrayAdapter<String> {

    // Member Variable - Stores the List of Videos.
    private String[] mVideos;
    private final Context mContext;

    // Constructor - Initializes the List of Videos.
    VideosAdapter(Context context) {
        super(context, -1);
        mContext = context;
    }

    // Sets the List of Videos & Notifies the Adapter that Data has changed.
    public void setmVideos(String[] videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    // Populates & Binds View with the correct Video's Link.
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        if (inflater != null) {
            rowView = inflater.inflate(R.layout.detail_video_item, parent, false);

            TextView videoView = rowView.findViewById(R.id.detail_video_number);
            String trailerNumber = mContext.getString(R.string.detail_trailer_label)
                    + " " + String.valueOf(position + 1);
            videoView.setText(trailerNumber);

            handleItemClickEvent(rowView, position);
        }
        //noinspection ConstantConditions
        return rowView;
    }

    // Helper Method - Handles Click Events - Plays Video in Youtube App if exist, Otherwise in Browser App.
    private void handleItemClickEvent(View rowView, final int position) {
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoId = mVideos[position];
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                intent.putExtra("VIDEO_ID", videoId);
                mContext.startActivity(intent);
            }
        });
    }

    // Returns the Total Number of Videos.
    @Override
    public int getCount() {
        if (mVideos != null) return mVideos.length;
        return 0;
    }
}
