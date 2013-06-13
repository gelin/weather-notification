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

package ru.gelin.android.weather.notification;

class ParcelableWeatherKeys {
    
    private ParcelableWeatherKeys() {
        //avoid instantiation
    }

    /** Key for location. */
    static final String LOCATION = "weather_location";
    /** Key for time. */
    static final String TIME = "weather_time";
    /** Key for query time. */
    static final String QUERY_TIME = "weather_query_time";
    /** Key for number of conditions */
    static final String CONDITIONS_NUMBER = "weather_conditions";
    
    /** Key for condition text. */
    static final String CONDITION_TEXT = "weather_condition_text";
    /** Key for condition type. */
    static final String CONDITION_TYPES = "weather_condition_types";

    /** Key for temperature unit. */
    static final String TEMPERATURE_UNIT = "weather_temperature_unit";
    /** Key for current temp. */
    static final String CURRENT_TEMP = "weather_current_temp";
    /** Key for low temp. */
    static final String LOW_TEMP = "weather_low_temp";
    /** Key for high temp. */
    static final String HIGH_TEMP = "weather_high_temp";

    /** Key for humidity text. */
    static final String HUMIDITY_TEXT = "weather_humidity_text";
    /** Key for humidity value. */
    static final String HUMIDITY_VALUE = "weather_humidity_value";

    /** Key for wind text. */
    static final String WIND_TEXT = "weather_wind_text";
    /** Key for wind speed unit. */
    static final String WIND_SPEED_UNIT = "weather_wind_speed_unit";
    /** Key for wind speed. */
    static final String WIND_SPEED = "weather_wind_speed";
    /** Key for wind direction. */
    static final String WIND_DIR = "weather_wind_direction";

    /** Key for cloudiness unit. */
    static final String CLOUDINESS_UNIT = "weather_cloudiness_unit";
    /** Key for cloudiness value. */
    static final String CLOUDINESS_VALUE = "weather_cloudiness_value";

    /** Key for precipitation unit. */
    static final String PRECIPITATION_UNIT = "weather_precipitation_unit";
    /** Key for precipitation period. */
    static final String PRECIPITATION_PERIOD = "weather_precipitation_period";
    /** Key for precipitation value. */
    static final String PRECIPITATION_VALUE = "weather_precipitation_value";

    /** Key for forecast URL. */
    static final String FORECAST_URL = "weather_forecast_url";

}
