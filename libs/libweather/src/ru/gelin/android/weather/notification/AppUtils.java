package ru.gelin.android.weather.notification;

import android.content.Context;
import android.content.Intent;

/**
 *  Static methods to start main app services and activites.
 */
public class AppUtils {

    /** Main app package name */
    private static final String APP_PACKAGE_NAME = Tag.class.getPackage().getName();

    /** Main app activity class name */
    private static final String MAIN_ACTIVITIY_CLASS_NAME =
            APP_PACKAGE_NAME + ".app.MainActivity";

    /** Intent action to start the service */
    private static String ACTION_START_UPDATE_SERVICE =
            APP_PACKAGE_NAME + ".ACTION_START_UPDATE_SERVICE";

    /** Verbose extra name for the service start intent. */
    public static String EXTRA_VERBOSE = "verbose";
    /** Force extra name for the service start intent. */
    public static String EXTRA_FORCE = "force";

    /**
     *  Returns intent to start the main activity.
     */
    public static Intent getMainActivityIntent() {
        Intent startIntent = new Intent();
        startIntent.setClassName(APP_PACKAGE_NAME, MAIN_ACTIVITIY_CLASS_NAME);
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
        Intent startIntent = new Intent(ACTION_START_UPDATE_SERVICE);
        //startIntent.setClassName(UpdateService.class.getPackage().getName(), UpdateService.class.getName());
        startIntent.putExtra(EXTRA_VERBOSE, verbose);
        startIntent.putExtra(EXTRA_FORCE, force);
        context.startService(startIntent);
    }

    private AppUtils() {
        //avoid instantiation
    }

}
