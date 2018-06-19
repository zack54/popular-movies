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

import android.net.Uri;

/**
 * Defines the Favorite Movies Database's Tables & Columns Names & the ContentProvider's URIs.
 * Keeps the code organized.
 */
public class FavoriteMoviesContract {

    // Define ContentProvider's URIs Constant - Tells Clients how to access the Data.
    private static final String SCHEMA = "content://";
    public static final String AUTHORITY = "com.example.android.popularmovies";
    private static final Uri BASE_URI = Uri.parse(SCHEMA + AUTHORITY);

    // Private Constructor - Used To Never create an instance of FavoriteMoviesContract Class.
    private FavoriteMoviesContract() {
    }

    // Inner Class - Defines the Movies Table's Constants.
    public static final class Movies {

        // Constant - Defines the Table's Name.
        public static final String TABLE_NAME = "movies";

        // Constant - Defines the Columns's Name.
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // Constants - Defines the Table's Path & Content URI & to Access the Table's Data.
        public static final String PATH = TABLE_NAME;
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

        // Constants - Defines the MIME Type of the Content being accessed.
        public static final String MOVIES_DIRECTORY_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "." + PATH;
        public static final String MOVIE_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "." + PATH;
    }
}