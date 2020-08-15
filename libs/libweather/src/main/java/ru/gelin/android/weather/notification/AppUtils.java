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

package ru.gelin.android.weather.notification;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import static ru.gelin.android.weather.notification.Tag.TAG;

/**
 *  Static methods to start main app services and activites.
 */
public class AppUtils {

    /** Main app package name */
    private static final String APP_PACKAGE_NAME = Tag.class.getPackage().getName();

    /** Intent action to start the service */
    public static String ACTION_START_UPDATE_SERVICE =
            APP_PACKAGE_NAME + ".ACTION_START_UPDATE_SERVICE";

    /** Intent action to start the main activity */
    public static String ACTION_START_MAIN_ACTIVITY =
            APP_PACKAGE_NAME + ".ACTION_START_MAIN_ACTIVITY";

    /** Verbose extra name for the service start intent. */
    public static String EXTRA_VERBOSE = "verbose";
    /** Force extra name for the service start intent. */
    public static String EXTRA_FORCE = "force";

    static final int JOB_ID = 1;
    static final long JOB_DELAY = 5 * 1000;       // want to start service in 5 seconds
    static final long JOB_UPLOAD_BYTES = 256;
    static final long JOB_DOWNLOAD_BYTES = 4 * 1024;
    static final String JOB_SERVICE_CLASS = "ru.gelin.android.weather.notification.app.UpdateJobService";

    /**
     *  Returns intent to start the main activity.
     */
    public static Intent getMainActivityIntent() {
        Intent startIntent = new Intent(ACTION_START_MAIN_ACTIVITY);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return startIntent;
    }

    /**
     *  Starts the main activity.
     */
    public static void startMainActivity(Context context) {
        context.startActivity(getMainActivityIntent());
    }

    /**
     *  Starts the update service.
     */
    public static void startUpdateService(Context context) {
        startUpdateService(context, false, false);
    }

    /**
     *  Starts the update service.
     *  If the verbose is true, the update errors will be displayed as toasts.
     */
    public static void startUpdateService(Context context, boolean verbose) {
        startUpdateService(context, verbose, false);
    }

    /**
     *  Starts the service.
     *  If the verbose is true, the update errors will be displayed as toasts.
     *  If the force is true, the update will start even when the weather is
     *  not expired.
     */
    public static void startUpdateService(Context context, boolean verbose, boolean force) {
        try {
            context.startService(getUpdateServiceIntent(context, verbose, force));
        } catch (IllegalStateException e) {
            Log.w(TAG, "Failed to start background service, it's illegal now");
            if (Build.VERSION.SDK_INT >= 21) {
                scheduleServiceJob(context);
            }
        }
    }

    /**
     * Creates intent to start the update service
     */
    public static Intent getUpdateServiceIntent(Context context, boolean verbose, boolean force) {
        Intent startIntent = new Intent(ACTION_START_UPDATE_SERVICE);
        startIntent.setPackage(APP_PACKAGE_NAME);
        //startIntent.setClassName(UpdateService.class.getPackage().getName(), UpdateService.class.getName());
        startIntent.putExtra(EXTRA_VERBOSE, verbose);
        startIntent.putExtra(EXTRA_FORCE, force);
        return startIntent;
    }

    private AppUtils() {
        //avoid instantiation
    }

    static void scheduleServiceJob(Context context) {
        Log.d(TAG, "Scheduling update service start");
        JobInfo jobInfo = getJobInfo(context);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    static JobInfo getJobInfo(Context context) {
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(context, JOB_SERVICE_CLASS));
        builder
            .setMinimumLatency(0)
            .setOverrideDeadline(JOB_DELAY)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true);
        if (Build.VERSION.SDK_INT >= 28) {
            builder
                .setEstimatedNetworkBytes(JOB_DOWNLOAD_BYTES, JOB_UPLOAD_BYTES)
                .setPrefetch(true);
        }
        return builder.build();
    }

}
