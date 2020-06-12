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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.MovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  Utility to layout weather values to view.
 */
public class WeatherLayout extends AbstractWeatherLayout {

    /** View to bind. */
    View view;

    /**
     *  Creates the utility for specified context.
     */
    public WeatherLayout(Context context, View view) {
        super(context);
        this.view = view;
    }

    @Override
    public int getTextColor() {
        return NO_CHANGE_COLOR;
    }

    @Override
    public int getBackColor() {
        return NO_CHANGE_COLOR;
    }

    @Override
    public void setBackColor(int viewId, int color) {
        // don't want to change color here
    }

    @Override
    public void setText(int viewId, CharSequence text, int color) {
        TextView textView = (TextView)this.view.findViewById(viewId);
        if (textView == null) {
            return;
        }
        if (text == null) {
            textView.setText("");
            return;
        }
        textView.setText(text);
    }

    @Override
    public void setIcon(int viewId, Drawable drawable, int level) {
        ImageView imageView = (ImageView)this.view.findViewById(viewId);
        if (imageView == null) {
            return;
        }
        imageView.setImageDrawable(drawable);
        imageView.setImageLevel(level);
    }

    @Override
    public void setVisibility(int viewId, int visibility) {
        View view = this.view.findViewById(viewId);
        if (view == null) {
            return;
        }
        view.setVisibility(visibility);
    }

    @Override
    public void setMovementMethod(int viewId, MovementMethod method) {
        View view = this.view.findViewById(viewId);
        if (!(view instanceof TextView)) {
            return;
        }
        ((TextView)view).setMovementMethod(method);
    }

}
