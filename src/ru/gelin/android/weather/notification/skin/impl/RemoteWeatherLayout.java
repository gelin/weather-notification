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

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.widget.RemoteViews;

/**
 *  Utility to layout weather values to remove view.
 */
public class RemoteWeatherLayout extends AbstractWeatherLayout {
    
    /** View to bind. */
    RemoteViews views;
    
    /** Ids of views to update. */
    final Set<Integer> ids = new HashSet<Integer>();
    
    /**
     *  Creates the utility for specified context.
     */
    public RemoteWeatherLayout(Context context, RemoteViews views) {
        super(context);
        this.views = views;
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        this.ids.add(ids.id("condition"));
        this.ids.add(ids.id("humidity"));
        this.ids.add(ids.id("wind"));
        this.ids.add(ids.id("temp"));
        this.ids.add(ids.id("current_temp"));
        this.ids.add(ids.id("current_temp_alt"));
        this.ids.add(ids.id("high_temp"));
        this.ids.add(ids.id("low_temp"));
        this.ids.add(ids.id("forecasts"));
        this.ids.add(ids.id("forecast_1"));
        this.ids.add(ids.id("forecast_2"));
        this.ids.add(ids.id("forecast_3"));
        this.ids.add(ids.id("forecast_day_1"));
        this.ids.add(ids.id("forecast_day_2"));
        this.ids.add(ids.id("forecast_day_3"));
        this.ids.add(ids.id("forecast_high_temp_1"));
        this.ids.add(ids.id("forecast_high_temp_2"));
        this.ids.add(ids.id("forecast_high_temp_3"));
        this.ids.add(ids.id("forecast_low_temp_1"));
        this.ids.add(ids.id("forecast_low_temp_2"));
        this.ids.add(ids.id("forecast_low_temp_3"));
    }
    
    @Override
    protected void setText(int viewId, String text) {
        if (skipView(viewId)) { //TODO: how to determine if the view is absent?
            return;
        }
        if (text == null) {
            views.setTextViewText(viewId, "");
            return;
        }
        views.setTextViewText(viewId, text);
    }
    
    @Override
    protected void setVisibility(int viewId, int visibility) {
        if (skipView(viewId)) { //TODO: how to determine if the view is absent?
            return;
        }
        views.setViewVisibility(viewId, visibility);
    }
    
    boolean skipView(int viewId) {
        return !this.ids.contains(viewId);
    }

}
