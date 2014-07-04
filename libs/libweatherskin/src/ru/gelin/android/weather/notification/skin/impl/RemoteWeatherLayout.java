/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.weather.notification.skin.impl;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.text.method.MovementMethod;
import android.widget.RemoteViews;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *  Utility to layout weather values to remove view.
 */
public class RemoteWeatherLayout extends AbstractWeatherLayout {
    
    /** Separator between text values */
    static final String SEPARATOR = "   ";
    
    /** View to bind. */
    RemoteViews views;
    
    /** Ids of views to update. */
    final Set<Integer> ids = new HashSet<Integer>();
    
    @Deprecated
    public RemoteWeatherLayout(Context context, RemoteViews views, 
            ru.gelin.android.weather.notification.skin.impl.TemperatureUnit unit) {
        this(context, views, TemperatureType.valueOf(unit));
    }
    
    /**
     *  Creates the utility for specified context.
     */
    public RemoteWeatherLayout(Context context, RemoteViews views, TemperatureType unit) {
        super(context);
        this.views = views;
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        this.ids.add(ids.id("condition"));
        this.ids.add(ids.id("condition_icon"));
        this.ids.add(ids.id("wind"));
        this.ids.add(ids.id("humidity"));
        this.ids.add(ids.id("temp"));
        this.ids.add(ids.id("current_temp"));
        this.ids.add(ids.id("high_temp"));
        this.ids.add(ids.id("low_temp"));
        this.ids.add(ids.id("forecasts"));
//        this.ids.add(ids.id("forecast_1"));
        this.ids.add(ids.id("forecast_day_1"));
        this.ids.add(ids.id("forecast_condition_icon_1"));
        this.ids.add(ids.id("forecast_high_temp_1"));
        this.ids.add(ids.id("forecast_low_temp_1"));
//        this.ids.add(ids.id("forecast_2"));
        this.ids.add(ids.id("forecast_day_2"));
        this.ids.add(ids.id("forecast_condition_icon_2"));
        this.ids.add(ids.id("forecast_high_temp_2"));
        this.ids.add(ids.id("forecast_low_temp_2"));
//        this.ids.add(ids.id("forecast_3"));
        this.ids.add(ids.id("forecast_day_3"));
        this.ids.add(ids.id("forecast_condition_icon_3"));
        this.ids.add(ids.id("forecast_high_temp_3"));
        this.ids.add(ids.id("forecast_low_temp_3"));
        switch(unit) {      //TODO: remove multiple appearance of this switch
        case C:
        case F:
            this.ids.add(ids.id("update_time_short"));
            break;
        case CF:
        case FC:
            this.ids.add(ids.id("current_temp_alt"));
            break;
        }
    }
    
    @Override
    protected void setText(int viewId, CharSequence text) {
        if (skipView(viewId)) { //TODO: how to determine if the view is absent?
            return;
        }
        if (text == null) {
            views.setTextViewText(viewId, "");
            return;
        }
        views.setTextViewText(viewId, text);
    }

    @Override
    protected void setIcon(int viewId, Drawable drawable, int level) {
        if (skipView(viewId)) {
            drawable.setLevel(level);
            Drawable bitmapDrawable = drawable.getCurrent();
            while (bitmapDrawable instanceof DrawableContainer) {
                bitmapDrawable = drawable.getCurrent();
            }
            if (!(bitmapDrawable instanceof BitmapDrawable)) {
                return;
            }
            views.setImageViewBitmap(viewId, ((BitmapDrawable) bitmapDrawable).getBitmap());
        }
    }

    @Override
    protected void setVisibility(int viewId, int visibility) {
        if (skipView(viewId)) { //TODO: how to determine if the view is absent?
            return;
        }
        views.setViewVisibility(viewId, visibility);
    }

    @Override
    protected void setMovementMethod(int viewId, MovementMethod method) {
        return;
    }
    
    boolean skipView(int viewId) {
        return !this.ids.contains(viewId);
    }
    
    protected void bindUpdateTime(Date update) {
        if (update.getTime() == 0) {
            setText(id("update_time_short"), "");
            return;
        }
        long minutes = (new Date().getTime() - update.getTime()) / 60000;
        String text = MessageFormat.format(this.context.getString(string("update_time_short")), minutes);
        setText(id("update_time_short"), text);
    }
    
}
