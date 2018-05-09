package com.example.android.popularmovies.model;

public class Movie {

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

    public Movie(double mVoteAverage, String mPosterPath, String mOriginalTitle, String mOverview, String mReleaseDate) {
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

}
