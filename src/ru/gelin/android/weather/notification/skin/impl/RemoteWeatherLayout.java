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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;

import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;

/**
 *  Utility to layout weather values to remove view.
 */
public class RemoteWeatherLayout extends AbstractWeatherLayout {
    
    /** Separator between text values */
    static final String SEPARATOR = "   ";
    
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
        this.ids.add(ids.id("forecasts_text"));
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
    
    @Override
    protected void bindForecasts(Weather weather, UnitSystem unit) {
        setVisibility(id("forecasts_text"), View.VISIBLE);
        StringBuilder forecastsText = new StringBuilder();
        for (int i = 1; i < 4; i++) {
            if (weather.getConditions().size() <= i) {
                break;
            }
            WeatherCondition forecast = weather.getConditions().get(i);
            Temperature temp = forecast.getTemperature();
            Date day = addDays(weather.getTime(), i);
            forecastsText.append(context.getString(string("forecast_text"), 
                    day, 
                    tempFormat.format(temp.getHigh()), 
                    tempFormat.format(temp.getLow())));
            forecastsText.append(SEPARATOR);
        }
        setText(id("forecasts_text"), forecastsText.toString());
    }

}
