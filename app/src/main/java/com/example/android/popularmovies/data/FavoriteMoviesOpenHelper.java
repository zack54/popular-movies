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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Defines & Updates the Favorite Movies Database's Schema.
 * Creates the Favorite Movies Database
 */
public class FavoriteMoviesOpenHelper extends SQLiteOpenHelper {

    // Constant - Defines the Database's Name.
    private static final String DATABASE_NAME = "favorite_movies.db";

    // Constant - Defines the Database's Version.
    private static final int DATABASE_VERSION = 1;

    // Constructor.
    FavoriteMoviesOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Defines the Database's Schema - Called when the Database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Defines a simple SQL Statement that will create a Movies Table.
        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + FavoriteMoviesContract.Movies.TABLE_NAME + " (" +
                        FavoriteMoviesContract.Movies.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        FavoriteMoviesContract.Movies.COLUMN_VOTE_AVERAGE + " FLOAT NOT NULL, " +
                        FavoriteMoviesContract.Movies.COLUMN_POSTER + " BLOB, " +
                        FavoriteMoviesContract.Movies.COLUMN_ORIGINAL_TITLE + " TINYTEXT NOT NULL," +
                        FavoriteMoviesContract.Movies.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                        FavoriteMoviesContract.Movies.COLUMN_RELEASE_DATE + " TINYTEXT NOT NULL);";

        // Defines a simple SQL Statement that will create a Videos Table.
        final String SQL_CREATE_VIDEOS_TABLE =
                "CREATE TABLE " + FavoriteMoviesContract.Videos.TABLE_NAME + " (" +
                        FavoriteMoviesContract.Videos._ID + " INTEGER PRIMARY KEY, " +
                        FavoriteMoviesContract.Videos.COLUMN_VIDEO_PATH + " TEXT NOT NULL," +
                        FavoriteMoviesContract.Videos.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                        "FOREIGN KEY(" + FavoriteMoviesContract.Videos.COLUMN_MOVIE_ID + ")" + " REFERENCES " +
                        FavoriteMoviesContract.Movies.TABLE_NAME + "(" + FavoriteMoviesContract.Movies.COLUMN_ID + ")" +
                        ");";

        // Defines a simple SQL Statement that will create a Reviews Table.
        final String SQL_CREATE_REVIEWS_TABLE =
                "CREATE TABLE " + FavoriteMoviesContract.Reviews.TABLE_NAME + " (" +
                        FavoriteMoviesContract.Reviews._ID + " INTEGER PRIMARY KEY, " +
                        FavoriteMoviesContract.Reviews.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL," +
                        FavoriteMoviesContract.Reviews.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL," +
                        FavoriteMoviesContract.Reviews.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                        "FOREIGN KEY(" + FavoriteMoviesContract.Reviews.COLUMN_MOVIE_ID + ")" + " REFERENCES " +
                        FavoriteMoviesContract.Movies.TABLE_NAME + "(" + FavoriteMoviesContract.Movies.COLUMN_ID + ")" +
                        ");";

        // Executes the SQL Statement - Creates the DataBase's Tables.
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    // Updates the Database's Schema - Called when Database's Version is incremented.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // Drops Current Database's Tables then Re-Creates it (since it only cache data stored online)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.Movies.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.Videos.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.Reviews.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
