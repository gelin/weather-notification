package ru.gelin.android.weather.notification;

import ru.gelin.android.weather.UnitSystem;

/**
 *  Constants for preference keys.
 */
public interface PreferenceKeys {

    /** Enable notification preference key */
    public static final String ENABLE_NOTIFICATION = "enable_notification";
    /** Enable notification default value */
    public static final boolean ENABLE_NOTIFICATION_DEFAULT = true;
    
    /** Refresh interval preference key */
    public static final String REFRESH_INTERVAL = "refresh_interval";
    /** Refresh interval default value */
    public static final String REFRESH_INTERVAL_DEFAULT = "1H"; //change to enum
    
    /** Notification style preference key */
    public static final String NOTIFICATION_STYLE = "notification_style";
    /** Notification style default value */
    public static final String NOTIFICATION_STYLE_DEFAULT = NotificationStyle.BLACK_TEXT.toString();
    
    /** Auto location preferences key */
    static final String AUTO_LOCATION = "auto_location";
    /** Auto location default value */
    static final boolean AUTO_LOCATION_DEFAULT = true;
    
    /** Manual location preferences key */
    static final String LOCATION = "location";
    /** Manual location default value */
    static final String LOCATION_DEFAULT = "";
    
    /** Unit system preference name */
    static final String UNIT_SYSTEM = "unit_system";
    /** Unit system default value */
    static final String UNIT_SYSTEM_DEFAULT = UnitSystem.SI.toString();

}
