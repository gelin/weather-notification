/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.weather.notification.skin.impl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.MovementMethod;
import android.widget.RemoteViews;

import java.util.Date;

/**
 *  Utility to layout weather values to remove view.
 */
public class RemoteWeatherLayout extends AbstractWeatherLayout {
    
    static final int MAIN_ICON = 64;
    static final int FORECAST_ICON = 16;

    /** View to bind. */
    RemoteViews views;

    /** Text style */
    NotificationStyler styler;

    /**
     *  Creates the utility for specified context.
     */
    public RemoteWeatherLayout(Context context, RemoteViews views, NotificationStyler styler) {
        super(context);
        this.views = views;
        this.styler = styler;
    }

    @Override
    protected int getTextColor() {
        return this.styler.getTextStyle().getTextColor();
    }

    @Override
    protected void setText(int viewId, CharSequence text, int color) {
        if (skipView(viewId)) {
            return;
        }
        if (text == null) {
            views.setTextViewText(viewId, "");
            return;
        }
        if (color != 0) {
            views.setTextColor(viewId, color);
        }
        views.setTextViewText(viewId, text);
    }

    @Override
    protected int getMainIconSize() {
        return MAIN_ICON;
    }

    @Override
    protected int getForecastIconSize() {
        return FORECAST_ICON;
    }

    @Override
    protected void setIcon(int viewId, Drawable drawable, int level) {
        if (skipView(viewId)) {
            return;
        }
        drawable.setLevel(level);
        views.setImageViewBitmap(viewId, Drawable2Bitmap.convert(drawable));
    }

    @Override
    protected void setVisibility(int viewId, int visibility) {
        if (skipView(viewId)) { //TODO: how to determine if the view is absent?
            return;
        }
        views.setViewVisibility(viewId, visibility);
    }

    @Override
    protected void setMovementMethod(int viewId, MovementMethod method) {
        return;
    }
    
    boolean skipView(int viewId) {
        return !this.styler.isViewInLayout(viewId);
    }
    
    protected void bindUpdateTime(Date update) {
        if (update.getTime() == 0) {
            setText(id("update_time_short"), "", NO_CHANGE_COLOR);
            return;
        }
        String text = getContext().getString(string("update_time_short"), update);
        setText(id("update_time_short"), text, NO_CHANGE_COLOR);
    }
    
}
