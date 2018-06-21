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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.popularmovies.data.FavoriteMoviesContract.Movies;
import static com.example.android.popularmovies.data.FavoriteMoviesContract.Videos;
import static com.example.android.popularmovies.data.FavoriteMoviesContract.Reviews;


/**
 * Loads & Displays Favorite Movies more Efficiently.
 * Shares Favorite Movies with other Apps if needed.
 */
public class FavoriteMoviesContentProvider extends ContentProvider {

    // Constants - Define Integer Codes for a Table & for a Single Row, Used to match URIs.
    public static final int MOVIES_CODE = 100;
    public static final int MOVIE_WITH_ID_CODE = 101;
    public static final int VIDEOS_CODE = 200;
    public static final int REVIEWS_CODE = 300;

    // Member Variable - Stores the URI Matcher.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Member Variable - Store a Reference to the Data Source.
    private FavoriteMoviesOpenHelper mFavoriteMoviesOpenHelper;

    // Helper Method - Associates URI's with Integer Codes.
    private static UriMatcher buildUriMatcher() {

        // Initializes a UriMatcher with no matches by passing in NO_MATCH to the constructor
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Adds Matches for the Movies Table & for a single Movie by ID.
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, Movies.PATH, MOVIES_CODE);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, Movies.PATH + "/#", MOVIE_WITH_ID_CODE);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, Videos.PATH, VIDEOS_CODE);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, Reviews.PATH, REVIEWS_CODE);

        return uriMatcher;
    }

    // Initializes the Underlying Data Source.
    @Override
    public boolean onCreate() {
        mFavoriteMoviesOpenHelper = new FavoriteMoviesOpenHelper(getContext());
        return true;
    }

    // Requests Data by URI - Validates Data by making sure it only Responds to valid URIs.
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase favoriteMoviesDatabase = mFavoriteMoviesOpenHelper.getReadableDatabase();
        Cursor returnedCursor;

        switch (sUriMatcher.match(uri)) {
            case MOVIES_CODE:
                returnedCursor = favoriteMoviesDatabase.query(Movies.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null);
                break;
            case MOVIE_WITH_ID_CODE:
                returnedCursor = favoriteMoviesDatabase.query(Movies.TABLE_NAME,
                        projection,
                        Movies.COLUMN_ID + " = ?",
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case VIDEOS_CODE:
                returnedCursor = favoriteMoviesDatabase.query(Videos.TABLE_NAME,
                        projection,
                        Videos.COLUMN_MOVIE_ID + " = ?",
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case REVIEWS_CODE:
                returnedCursor = favoriteMoviesDatabase.query(Reviews.TABLE_NAME,
                        projection,
                        Reviews.COLUMN_MOVIE_ID + " = ?",
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Register the ContentResolver's Listener to watch a Content URI for Changes.
        if (getContext() != null) {
            returnedCursor.setNotificationUri(getContext().getContentResolver(), Movies.CONTENT_URI);
        }
        return returnedCursor;
    }

    // Inserts Single Row by URI - Validates Data by making sure it only Responds to valid URIs.
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase favoriteMoviesDatabase = mFavoriteMoviesOpenHelper.getWritableDatabase();
        Uri returnedUri;

        switch (sUriMatcher.match(uri)) {
            case MOVIES_CODE:
                long id = favoriteMoviesDatabase.insert(Movies.TABLE_NAME, null, values);
                if (id != -1) {
                    returnedUri = ContentUris.withAppendedId(Movies.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case VIDEOS_CODE:
                long videoId = favoriteMoviesDatabase.insert(Videos.TABLE_NAME, null, values);
                if (videoId != -1) {
                    returnedUri = ContentUris.withAppendedId(Videos.CONTENT_URI, videoId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case REVIEWS_CODE:
                long reviewId = favoriteMoviesDatabase.insert(Reviews.TABLE_NAME, null, values);
                if (reviewId != -1) {
                    returnedUri = ContentUris.withAppendedId(Reviews.CONTENT_URI, reviewId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notifies the Registered Listener that a Row was inserted & Sync Changes.
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnedUri;
    }

    // Deletes Data by URI - Validates Data by making sure it only Responds to valid URIs.
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final SQLiteDatabase favoriteMoviesDatabase = mFavoriteMoviesOpenHelper.getWritableDatabase();
        int rowsDeleted;

        switch (sUriMatcher.match(uri)) {
            case MOVIE_WITH_ID_CODE:
                rowsDeleted = favoriteMoviesDatabase.delete(Movies.TABLE_NAME,
                        Movies.COLUMN_ID + " = ?",
                        selectionArgs);
                break;
            case VIDEOS_CODE:
                rowsDeleted = favoriteMoviesDatabase.delete(Videos.TABLE_NAME,
                        Videos.COLUMN_MOVIE_ID + " = ?",
                        selectionArgs);
                break;
            case REVIEWS_CODE:
                rowsDeleted = favoriteMoviesDatabase.delete(Reviews.TABLE_NAME,
                        Reviews.COLUMN_MOVIE_ID + " = ?",
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notifies the Registered Listener that Row(s) was deleted & Sync Changes.
        if (rowsDeleted > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    // Updates Data by URI - Validates Data by making sure it only Responds to valid URIs.
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update in Sunshine");
    }

    // Returns the MIME Type of the Content being accessed - Directory / Item Type.
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Sunshine.");
    }

    // Closes the Database.
    @Override
    public void shutdown() {
        mFavoriteMoviesOpenHelper.close();
        super.shutdown();
    }
}
