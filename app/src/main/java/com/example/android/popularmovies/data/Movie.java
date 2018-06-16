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

package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Defines the Movie Data Model.
 * Implement Parcelable - So a Movie Object can be passed between Activities.
 */
public class Movie implements Parcelable {

    // Constant - Makes the Android system leverage the serialization code.
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    // Member Variables - Holds the Movie's Properties.
    private final int mId;
    private final double mVoteAverage;
    private final String mPosterPath;
    private final String mOriginalTitle;
    private final String mOverview;
    private final String mReleaseDate;

    // Public Constructor - Initializes the Movie's Properties.
    public Movie(int id, double mVoteAverage, String mPosterPath, String mOriginalTitle, String mOverview,
                 String mReleaseDate) {
        this.mId = id;
        this.mVoteAverage = mVoteAverage;
        this.mPosterPath = mPosterPath;
        this.mOriginalTitle = mOriginalTitle;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
    }

    // Private Constructor - De-Serializes the Parcel object & Reconstructs the original Properties.
    private Movie(Parcel in) {
        this.mId = in.readInt();
        this.mVoteAverage = in.readDouble();
        this.mPosterPath = in.readString();
        this.mOriginalTitle = in.readString();
        this.mOverview = in.readString();
        this.mReleaseDate = in.readString();
    }

    public int getmId() {
        return mId;
    }

    public double getmVoteAverage() {
        return mVoteAverage;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    // Serializes the Movie Object - Stores the Movie's Properties to a Parcel Object.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeDouble(mVoteAverage);
        dest.writeString(mPosterPath);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
    }

    // Sets a Flag indicating special data types.
    @Override
    public int describeContents() {
        return 0;
    }

}
