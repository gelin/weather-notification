/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather.notification.skin.impl;


/**
 *  Enumeration with notification text styles.
 */
public enum NotificationTextStyle {

    BLACK_TEXT(0xff000000),
    WHITE_TEXT(0xffffffff);

    int textColor;
    
    private NotificationTextStyle(int textColor) {
        this.textColor = textColor;
    }

    /**
     *  Returns color of the text for this notification style.
     */
    public int getTextColor() {
        return this.textColor;
    }
    
}
