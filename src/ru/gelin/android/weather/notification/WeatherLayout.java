package ru.gelin.android.weather.notification;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 *  Utility to layout weather values to view.
 */
public class WeatherLayout extends AbstractWeatherLayout {
    
    /** View to bind. */
    View view;
    
    /**
     *  Creates the utility for specified context.
     */
    public WeatherLayout(Context context, View view) {
        super(context);
        this.view = view;
    }
    
    @Override
    protected void setText(int viewId, String text) {
        TextView textView = (TextView)this.view.findViewById(viewId);
        if (textView == null) {
            return;
        }
        if (text == null) {
            textView.setText("");
            return;
        }
        textView.setText(text);
    }
    
    @Override
    protected void setVisibility(int viewId, int visibility) {
        View view = this.view.findViewById(viewId);
        if (view == null) {
            return;
        }
        view.setVisibility(visibility);
    }

}
