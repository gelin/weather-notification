package ru.gelin.android.weather.notification.skin.builtin;

/**
 *  Constants for preference keys.
 */
public interface PreferenceKeys {

    /** Notification icon style preference key */
    public static final String NOTIFICATION_ICON_STYLE = "notification_icon_style";
    /** Notification icon style default value */
    public static final String NOTIFICATION_ICON_STYLE_DEFAULT = NotificationStyle.BLACK_TEXT.toString();
    
    /** Notification text style preference key */
    public static final String NOTIFICATION_TEXT_STYLE = "notification_text_style";
    /** Notification text style default value */
    public static final String NOTIFICATION_TEXT_STYLE_DEFAULT = NotificationStyle.BLACK_TEXT.toString();
    
    /** Temperature unit preference name */
    static final String TEMP_UNIT = "temp_unit";
    /** Temperature unit default value */
    static final String TEMP_UNIT_DEFAULT = TemperatureUnit.C.toString();

}
