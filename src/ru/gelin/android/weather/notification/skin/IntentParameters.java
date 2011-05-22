package ru.gelin.android.weather.notification.skin;

import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.Tag;

public class IntentParameters {

    /** Intent action which should be accepted by the receiver */ 
    public static final String ACTION_WEATHER_UPDATE =
        Tag.class.getPackage().getName() + ".ACTION_WEATHER_UPDATE";
    
    /** Intent extra which contains {@link Weather} */ 
    public static final String EXTRA_WEATHER =
        Tag.class.getPackage().getName() + ".EXTRA_WEATHER";
    
    /** Intent extra which contains boolean flag */ 
    public static final String EXTRA_ENABLE_NOTIFICATION =
        Tag.class.getPackage().getName() + ".EXTRA_ENABLE_NOTIFICATION";
    
    /** Intent action for the skin configuration activity */
    public static final String ACTION_WEATHER_SKIN_PREFERENCES =
        Tag.class.getPackage().getName() + ".ACTION_WEATHER_SKIN_PREFERENCES";

}
