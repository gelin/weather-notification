package ru.gelin.android.weather.notification.skin.builtin;

import ru.gelin.android.weather.notification.R;

public enum NotificationStyle {

    BLACK_TEXT(R.drawable.temp_icon_black, R.layout.notification_black),
    WHITE_TEXT(R.drawable.temp_icon_white, R.layout.notification_white);

    int iconRes;
    int layoutRes;
    
    private NotificationStyle(int iconRes, int layoutRes) {
        this.iconRes = iconRes;
        this.layoutRes = layoutRes;
    }
    
    /**
     *  Returns resource ID of the status bar icon.
     */
    public int getIconRes() {
        return this.iconRes;
    }
    
    /**
     *  Returns resource ID of the notification text layout.
     */
    public int getLayoutRes() {
        return this.layoutRes;
    }
    
}
