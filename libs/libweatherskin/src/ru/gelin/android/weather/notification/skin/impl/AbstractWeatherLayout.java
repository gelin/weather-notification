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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.View;
import ru.gelin.android.weather.*;
import ru.gelin.android.weather.TemperatureUnit;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.*;
import static ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory.STRING;

/**
 *  Utility to layout weather values.
 */
public abstract class AbstractWeatherLayout {

    static final String URL_TEMPLATE = "<a href=\"%s\">%s</a>";

    static final int MAIN_ICON = 48;
    static final int FORECAST_ICON = 16;

    static final int NO_CHANGE_COLOR = 0;

    /** Current context */
    protected Context context;
    /** ID factory */
    ResourceIdFactory ids;
    /** Condition text formatter */
    WeatherConditionFormat conditionFormat;
    /** Temperature formatter */
    TemperatureFormat tempFormat;
    /** Wind formatter */
    WindFormat windFormat;
    /** Humidity formatter */
    HumidityFormat humidityFormat;
    
    /**
     *  Creates the utility for specified context.
     */
    protected AbstractWeatherLayout(Context context) {
        this.context = context;
        this.ids = ResourceIdFactory.getInstance(context);
        this.conditionFormat = new WeatherConditionFormat(context);
        this.tempFormat = createTemperatureFormat();
        this.windFormat = new WindFormat(context);
        this.humidityFormat = new HumidityFormat(context);
    }
    
    /**
     *  Retreives "id/<name>" resource ID.
     */
    protected int id(String name) {
        return this.ids.id(name);
    }
    
    /**
     *  Retreives "string/<name>" resource ID.
     */
    protected int string(String name) {
        return this.ids.id(STRING, name);
    }
    
    /**
     *  Lays out the weather values on a view.
     */
    public void bind(Weather weather) {
        if (weather.isEmpty()) {
            emptyViews();
        } else {
            bindViews(weather);
        }

    }
    
    void bindViews(Weather weather) {
        bindUpdateTime(weather.getQueryTime());
        setText(id("location"), weather.getLocation().getText(), getTextColor());
        
        if (weather.getConditions().size() <= 0) {
            return;
        }
        WeatherCondition currentCondition = weather.getConditions().get(0);
        setIcon(id("condition_icon"), this.conditionFormat.getDrawable(currentCondition), MAIN_ICON);
        setText(id("condition"), this.conditionFormat.getText(currentCondition), getTextColor());
        bindWindHumidity(currentCondition);
        
        SharedPreferences preferences = 
                PreferenceManager.getDefaultSharedPreferences(this.context);
        TemperatureType tempType = TemperatureType.valueOf(preferences.getString(
                TEMP_UNIT, TEMP_UNIT_DEFAULT));
        
        Temperature tempC = currentCondition.getTemperature(TemperatureUnit.C);
        Temperature tempF = currentCondition.getTemperature(TemperatureUnit.F);
        TemperatureUnit mainUnit = tempType.getTemperatureUnit();
        Temperature mainTemp = currentCondition.getTemperature(mainUnit);
        
        setVisibility(id("temp"), View.VISIBLE);
        setText(id("current_temp"), tempFormat.format(mainTemp.getCurrent(), tempType), getTextColor());
        switch(tempType) {      //TODO: remove multiple appearance of this switch
        case C: case F:
            setVisibility(id("current_temp_alt"), View.GONE);
            break;
        case CF:
            setText(id("current_temp_alt"),
                    tempFormat.format(tempF.getCurrent(), TemperatureType.F), getTextColor());
            setVisibility(id("current_temp_alt"), View.VISIBLE);
            break;
        case FC:
            setText(id("current_temp_alt"),
                    tempFormat.format(tempC.getCurrent(), TemperatureType.C), getTextColor());
            setVisibility(id("current_temp_alt"), View.VISIBLE);
            break;
        }
        setText(id("high_temp"), tempFormat.format(mainTemp.getHigh()), getTextColor());
        setText(id("low_temp"), tempFormat.format(mainTemp.getLow()), getTextColor());
        
        bindForecasts(weather, mainUnit);

        bindForecastUrl(weather);
    }
    
    protected void bindUpdateTime(Date update) {
        if (update.getTime() == 0) {
            setText(id("update_time"), "", NO_CHANGE_COLOR);
        } else if (isDate(update)) {
            setText(id("update_time"),
                    this.context.getString(string("update_date_format"), update), getTextColor());
        } else {
            setText(id("update_time"),
                    this.context.getString(string("update_time_format"), update), getTextColor());
        }
    }
    
    protected void bindWindHumidity(WeatherCondition currentCondition) {
        SharedPreferences preferences = 
            PreferenceManager.getDefaultSharedPreferences(this.context);
        
        WindUnit windUnit = WindUnit.valueOf(preferences.getString(
                WS_UNIT, WS_UNIT_DEFAULT));
        Wind wind = currentCondition.getWind(windUnit.getWindSpeedUnit());
        setText(id("wind"), this.windFormat.format(wind), getTextColor());
        
        Humidity humidity = currentCondition.getHumidity();
        setText(id("humidity"), this.humidityFormat.format(humidity), getTextColor());
    }
    
    protected void bindForecasts(Weather weather, ru.gelin.android.weather.TemperatureUnit unit) {
        setVisibility(id("forecasts"), View.VISIBLE);
        bindForecast(weather, unit, 1,
                id("forecast_1"),
                id("forecast_condition_icon_1"),
                id("forecast_day_1"),
                id("forecast_condition_1"),
                id("forecast_high_temp_1"), id("forecast_low_temp_1"));
        bindForecast(weather, unit, 2,
                id("forecast_2"),
                id("forecast_condition_icon_2"),
                id("forecast_day_2"),
                id("forecast_condition_2"),
                id("forecast_high_temp_2"), id("forecast_low_temp_2"));
        bindForecast(weather, unit, 3,
                id("forecast_3"),
                id("forecast_condition_icon_3"),
                id("forecast_day_3"),
                id("forecast_condition_3"),
                id("forecast_high_temp_3"), id("forecast_low_temp_3"));
    }
    
    void bindForecast(Weather weather, 
            ru.gelin.android.weather.TemperatureUnit unit, int i, 
            int groupId, int iconId, int dayId, int conditionId,
            int highTempId, int lowTempId) {
        if (weather.getConditions().size() > i) {
            setVisibility(groupId, View.VISIBLE);
            WeatherCondition forecastCondition = weather.getConditions().get(i);
            Date tomorrow = addDays(weather.getTime(), i);
            setIcon(iconId, this.conditionFormat.getDrawable(forecastCondition), FORECAST_ICON);
            setText(dayId, context.getString(string("forecast_day_format"), tomorrow), getTextColor());
            setText(conditionId, this.conditionFormat.getText(forecastCondition), getTextColor());
            Temperature forecastTemp = forecastCondition.getTemperature(unit);
            setText(highTempId, tempFormat.format(forecastTemp.getHigh()), getTextColor());
            setText(lowTempId, tempFormat.format(forecastTemp.getLow()), getTextColor());
        } else {
            setVisibility(groupId, View.GONE);
        }
    }

    void bindForecastUrl(Weather weather) {
        if (weather.getForecastURL() == null) {
            setVisibility(id("forecast_url"), View.INVISIBLE);
            return;
        }
        setVisibility(id("forecast_url"), View.VISIBLE);
        URL url = weather.getForecastURL();
        String link = String.format(URL_TEMPLATE, url.toString(), url.getHost());
        setMovementMethod(id("forecast_url"), LinkMovementMethod.getInstance());
        setText(id("forecast_url"), Html.fromHtml(link), NO_CHANGE_COLOR);
    }
    
    void emptyViews() {
        setText(id("update_time"), "", NO_CHANGE_COLOR);
        setText(id("location"), "", NO_CHANGE_COLOR);
        setText(id("condition"), context.getString(string("unknown_weather")), getTextColor());
        setText(id("humidity"), "", NO_CHANGE_COLOR);
        setText(id("wind"), "", NO_CHANGE_COLOR);
        setText(id("wind_humidity_text"), "", NO_CHANGE_COLOR);
        
        setVisibility(id("temp"), View.INVISIBLE);
        
        setVisibility(id("forecasts"), View.GONE);
        setVisibility(id("forecasts_text"), View.GONE);

        setVisibility(id("forecast_url"), View.INVISIBLE);
    }
    
    protected abstract void setText(int viewId, CharSequence text, int color);

    protected abstract void setIcon(int viewId, Drawable drawable, int level);
    
    protected abstract void setVisibility(int viewId, int visibility);

    protected abstract void setMovementMethod(int viewId, MovementMethod method);
    
    Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
    
    /**
     *  Returns true if the provided date has zero (0:00:00) time.
     */
    boolean isDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR) == 0 &&
                calendar.get(Calendar.MINUTE) == 0 &&
                calendar.get(Calendar.SECOND) == 0 &&
                calendar.get(Calendar.MILLISECOND) == 0;
    }
    
    /**
     *  Creates the temperature formatter.
     */
    protected TemperatureFormat createTemperatureFormat() {
        return new TemperatureFormat();
    }

    /**
     *  Returns the text color to be set on all text views.
     *  This color is passed to #setText() calls.
     */
    protected abstract int getTextColor();
    
}
