package ru.gelin.android.weather.notification;

import android.content.Context;
import android.widget.RemoteViews;

/**
 *  Utility to layout weather values to remove view.
 */
public class RemoteWeatherLayout extends AbstractWeatherLayout {
    
    /** View to bind. */
    RemoteViews views;
    
    /**
     *  Creates the utility for specified context.
     */
    public RemoteWeatherLayout(Context context, RemoteViews views) {
        super(context);
        this.views = views;
    }
    
    @Override
    void setText(int viewId, String text) {
        if (text == null) {
            views.setTextViewText(viewId, "");
            return;
        }
        views.setTextViewText(viewId, text);
    }
    
    void setVisibility(int viewId, int visibility) {
        views.setViewVisibility(viewId, visibility);
    }

}
