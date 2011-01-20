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
        if (skipView(viewId)) { //TODO: how to determine if the view is absent?
            return;
        }
        if (text == null) {
            views.setTextViewText(viewId, "");
            return;
        }
        views.setTextViewText(viewId, text);
    }
    
    void setVisibility(int viewId, int visibility) {
        if (skipView(viewId)) { //TODO: how to determine if the view is absent?
            return;
        }
        views.setViewVisibility(viewId, visibility);
    }
    
    boolean skipView(int viewId) {
        switch (viewId) {
        case R.id.condition:
        case R.id.humidity:
        case R.id.wind:
        case R.id.temp:
        case R.id.current_temp:
        case R.id.high_temp:
        case R.id.low_temp:
            return false;
        default:
            return true;
        }
    }

}
