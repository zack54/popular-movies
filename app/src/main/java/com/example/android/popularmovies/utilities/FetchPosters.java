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

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility functions - Handles loading & caching images.
 */
public class FetchPosters {

    // Constant - Holds the Images' base url.
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    // Constants - Holds the size of the images to query.
    public static final String X_LARGE_IMAGE_SIZE = "w780/";
    public static final String LARGE_IMAGE_SIZE = "w500/";
    public static final String MEDIUM_IMAGE_SIZE = "w342/";
    public static final String SMALL_IMAGE_SIZE = "w185";
    public static final String X_SMALL_IMAGE_SIZE = "w154";

    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mImageView.setImageBitmap(bitmap);
            mImageBitmap = bitmap;
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    // Fetches & Loads an Image into an ImageView.
    public Bitmap usingRelativePathAndSize(ImageView imageView, String relativePath,
                                           String size) {
        mImageView = imageView;
        String mFullImagePath = IMAGE_BASE_URL + size + relativePath;

        Picasso.get().load(mFullImagePath).into(target);
        return mImageBitmap;
    }
}
