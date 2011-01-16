package ru.gelin.android.weather.notification;

public enum NotificationStyle {

    BLACK_TEXT(R.drawable.temp_icon_black), WHITE_TEXT(R.drawable.temp_icon_white);

    int iconRes;
    
    private NotificationStyle(int iconRes) {
        this.iconRes = iconRes;
    }
    
    /**
     *  Returns resource ID of the status bar icon.
     */
    public int getIconRes() {
        return this.iconRes;
    }
    
}
