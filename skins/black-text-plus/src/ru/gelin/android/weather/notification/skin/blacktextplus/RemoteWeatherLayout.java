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

package ru.gelin.android.weather.notification.skin.blacktextplus;

import ru.gelin.android.weather.notification.AbstractWeatherLayout;
import android.content.Context;
import android.widget.RemoteViews;

/**
 *  Utility to layout weather values to remove view.
 */
public class RemoteWeatherLayout extends AbstractWeatherLayout {
    
    /** View to bind. */
    RemoteViews views;
    
    /**
     *  Creates the utility for specified context.
     */
    public RemoteWeatherLayout(Context context, RemoteViews views) {
        super(context);
        this.views = views;
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
        switch (viewId) {
        case R.id.condition:
        case R.id.humidity:
        case R.id.wind:
        case R.id.temp:
        case R.id.current_temp:
        case R.id.current_temp_alt:
        case R.id.high_temp:
        case R.id.low_temp:
        case R.id.forecasts:
        case R.id.forecast_1:
        case R.id.forecast_2:
        case R.id.forecast_3:
        case R.id.forecast_day_1:
        case R.id.forecast_day_2:
        case R.id.forecast_day_3:
        case R.id.forecast_high_temp_1:
        case R.id.forecast_high_temp_2:
        case R.id.forecast_high_temp_3:
        case R.id.forecast_low_temp_1:
        case R.id.forecast_low_temp_2:
        case R.id.forecast_low_temp_3:
            return false;
        default:
            return true;
        }
    }

}
