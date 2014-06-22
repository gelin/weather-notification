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
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.method.MovementMethod;
import android.view.View;
import android.widget.RemoteViews;
import ru.gelin.android.weather.*;
import ru.gelin.android.weather.TemperatureUnit;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.WS_UNIT;
import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.WS_UNIT_DEFAULT;

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
    
    @Deprecated
    public RemoteWeatherLayout(Context context, RemoteViews views, 
            ru.gelin.android.weather.notification.skin.impl.TemperatureUnit unit) {
        this(context, views, TemperatureType.valueOf(unit));
    }
    
    /**
     *  Creates the utility for specified context.
     */
    public RemoteWeatherLayout(Context context, RemoteViews views, TemperatureType unit) {
        super(context);
        this.views = views;
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        this.ids.add(ids.id("condition"));
        this.ids.add(ids.id("wind_humidity_text"));
        this.ids.add(ids.id("temp"));
        this.ids.add(ids.id("current_temp"));
        this.ids.add(ids.id("high_temp"));
        this.ids.add(ids.id("low_temp"));
        this.ids.add(ids.id("forecasts_text"));
        switch(unit) {      //TODO: remove multiple appearance of this switch
        case C:
        case F:
            this.ids.add(ids.id("update_time_short"));
            break;
        case CF:
        case FC:
            this.ids.add(ids.id("current_temp_alt"));
            break;
        }
    }
    
    @Override
    protected void setText(int viewId, CharSequence text) {
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
    protected void setIcon(int viewId, Drawable drawable, int level) {
        return; //TODO if necessary to display icons
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
        return !this.ids.contains(viewId);
    }
    
    protected void bindUpdateTime(Date update) {
        if (update.getTime() == 0) {
            setText(id("update_time_short"), "");
            return;
        }
        long minutes = (new Date().getTime() - update.getTime()) / 60000;
        String text = MessageFormat.format(this.context.getString(string("update_time_short")), minutes);
        setText(id("update_time_short"), text);
    }
    
    protected void bindWindHumidity(WeatherCondition currentCondition) {
        SharedPreferences preferences = 
            PreferenceManager.getDefaultSharedPreferences(this.context);

        WindUnit windUnit = WindUnit.valueOf(preferences.getString(
                WS_UNIT, WS_UNIT_DEFAULT));
        Wind wind = currentCondition.getWind(windUnit.getWindSpeedUnit());
        Humidity humidity = currentCondition.getHumidity();
        
        StringBuilder text = new StringBuilder();
        text.append(this.windFormat.format(wind));
        text.append(SEPARATOR);
        text.append(this.humidityFormat.format(humidity));
        setText(id("wind_humidity_text"), text.toString());
    }
    
    @Override
    protected void bindForecasts(Weather weather, TemperatureUnit unit) {
        setVisibility(id("forecasts_text"), View.VISIBLE);
        StringBuilder forecastsText = new StringBuilder();
        for (int i = 1; i < 4; i++) {
            if (weather.getConditions().size() <= i) {
                break;
            }
            WeatherCondition forecast = weather.getConditions().get(i);
            Temperature temp = forecast.getTemperature(unit);
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
