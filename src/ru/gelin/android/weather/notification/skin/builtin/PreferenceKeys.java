package ru.gelin.android.weather.notification.skin.builtin;

import ru.gelin.android.weather.UnitSystem;

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
    
    /** Unit system preference name */
    static final String UNIT_SYSTEM = "unit_system";
    /** Unit system default value */
    static final String UNIT_SYSTEM_DEFAULT = UnitSystem.SI.toString();

}
