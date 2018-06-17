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

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.JsonUtils;

/**
 * Exposes a list of Reviews.
 */
public class ReviewsAdapter extends ArrayAdapter<ContentValues> {

    // Member Variable - Stores the List of Reviews.
    private ContentValues[] mReviews;
    private final Context mContext;

    // Constructor - Initializes the List of Reviews.
    ReviewsAdapter(Context context) {
        super(context, -1);
        mContext = context;
    }

    // Sets the List of Reviews & Notifies the Adapter that Data has changed.
    public void setmReviews(ContentValues[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    // Populates & Binds View with the correct Video's Link.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        if (inflater != null) {
            rowView = inflater.inflate(R.layout.detail_review_item, parent, false);
            TextView author = rowView.findViewById(R.id.detail_review_author);
            TextView content = rowView.findViewById(R.id.detail_review_content);

            author.setText((String) mReviews[position].get(JsonUtils.REVIEW_AUTHOR));
            content.setText((String) mReviews[position].get(JsonUtils.REVIEW_CONTENT));
        }
        return rowView;
    }

    // Returns the Total Number of Reviews.
    @Override
    public int getCount() {
        if (mReviews != null) return mReviews.length;
        return 0;
    }
}
