package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private double mVoteAverage;
    private String mPosterPath;
    private String mOriginalTitle;
    private String mOverview;
    private String mReleaseDate;

    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    public Movie(double mVoteAverage, String mPosterPath, String mOriginalTitle, String mOverview,
                 String mReleaseDate) {
        this.mVoteAverage = mVoteAverage;
        this.mPosterPath = mPosterPath;
        this.mOriginalTitle = mOriginalTitle;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
    }

    public void setmVoteAverage(double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
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


    // responsible for de-serializing data to reconstruct the original object.
    protected Movie(Parcel in) {
        this.mVoteAverage = in.readDouble();
        this.mPosterPath = in.readString();
        this.mOriginalTitle = in.readString();
        this.mOverview = in.readString();
        this.mReleaseDate = in.readString();
    }

    // take in an input Parcel to be deserialized.
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

    // set bitwise flags indicating special data types
    @Override
    public int describeContents() {
        return 0;
    }

    // responsible for serializing the data.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mVoteAverage);
        dest.writeString(mPosterPath);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
    }

}
