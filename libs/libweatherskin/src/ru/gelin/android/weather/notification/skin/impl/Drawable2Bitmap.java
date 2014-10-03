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
