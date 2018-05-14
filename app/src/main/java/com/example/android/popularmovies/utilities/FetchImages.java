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

package com.example.android.popularmovies.utilities;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Utility functions - Handles loading & caching images.
 */
public class FetchImages {

    // Constant - Holds the Images' base url.
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    // Fetches & Loads an Image into an ImageView.
    public static void usingRelativePathAndSize(ImageView imageView, String relativePath,
                                                String size) {
        String fullImagePath = IMAGE_BASE_URL + size + relativePath;
        Picasso.get().load(fullImagePath).into(imageView);
    }
}
