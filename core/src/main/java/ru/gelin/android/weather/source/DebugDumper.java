

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

package ru.gelin.android.weather.source;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.notification.app.DebugActivity;
import ru.gelin.android.weather.notification.app.DebugSettings;
import ru.gelin.android.weather.notification.app.Tag;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 *  Saves the query result to a file.
 */
public class DebugDumper {

    static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss'Z'");
    static final Set<Character> BAD_CHARS = new HashSet<Character>();
    static final char REPLACEMENT = '_';
    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        BAD_CHARS.add('/');
        BAD_CHARS.add('?');
        BAD_CHARS.add('<');
        BAD_CHARS.add('>');
        BAD_CHARS.add('\\');
        BAD_CHARS.add(':');
        BAD_CHARS.add('*');
        BAD_CHARS.add('|');
        BAD_CHARS.add('"');
        BAD_CHARS.add('&');
        BAD_CHARS.add('=');
    }

    private final Context context;
    private final DebugSettings settings;
    private final File path;
    private final String prefix;

    public DebugDumper(Context context, String prefix) {
        this.context = context;
        this.settings = new DebugSettings(context);
        this.path = settings.getDebugDir();
        this.prefix = prefix;
    }

    public void dump(String url, String content) {
        if (!this.settings.isAPIDebug()) {
            return;
        }
        if (!checkAndRequestPermission()) {
            return;
        }
        File dumpFile = getDumpFile(url);
        File dir = dumpFile.getParentFile();
        if (!(dir.mkdirs() || dir.isDirectory())) {
            Log.w(Tag.TAG, "cannot create dir " + dir);
            return;
        }
        try {
            Log.d(Tag.TAG, "dumping to " + dumpFile);
            Writer out = new FileWriter(dumpFile);
            out.write(content);
            out.close();
        } catch (Exception e) {
            Log.w(Tag.TAG, "cannot create debug dump", e);
        }
    }

    File getDumpFile(String url) {
        StringBuilder fileName = new StringBuilder();
        fileName.append(DATE_FORMAT.format(new Date()));
        fileName.append(url.replace(this.prefix, ""));
        for (int i = 0; i < fileName.length(); i++) {
            if (BAD_CHARS.contains(fileName.charAt(i))) {
                fileName.setCharAt(i, REPLACEMENT);
            }
        }
        fileName.append(".txt");
        return new File(this.path, fileName.toString());
    }

    /**
     * Checks is the permission to write the dump file is granted, asks for it if necessary.
     * @return does the permission granted
     */
    boolean checkAndRequestPermission() {
        if (settings.isPermissionGranted()) {
            return true;
        }
        displayPermissionNotification();
        return false;
    }

    void displayPermissionNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.status_icon);
        builder.setContentTitle(context.getString(R.string.permission_required));
        builder.setContentText(context.getString(R.string.permission_required_details));

        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(false);
        builder.setAutoCancel(true);

        Intent intent = new Intent(context, DebugActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));

        //Lollipop notification on lock screen
        builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }

}
