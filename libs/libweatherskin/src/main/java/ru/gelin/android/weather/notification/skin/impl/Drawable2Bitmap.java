/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather.notification.skin.impl;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;

/**
 *  Utility class to convert Drawable from resources into Bitmap.
 */
public class Drawable2Bitmap {

    /**
     *  Extracts Bitmap from BitmapDrawable.
     *  Goes into DrawableContainer hierarchy using {@link android.graphics.drawable.Drawable#getCurrent()}.
     *  If the final Drawable is not BitmapDrawable, returns null.
     *  @param drawable BitmapDrawable or DrawableContainer
     *  @return Bitmap or null
     */
    public static Bitmap convert(Drawable drawable) {
        Drawable bitmapDrawable = drawable.getCurrent();
        while (bitmapDrawable instanceof DrawableContainer) {
            bitmapDrawable = drawable.getCurrent();
        }
        if (!(bitmapDrawable instanceof BitmapDrawable)) {
            return null;
        }
        return ((BitmapDrawable)bitmapDrawable).getBitmap();
    }

    private Drawable2Bitmap() {
        //avoid instantiation
    }

}
