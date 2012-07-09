/*
 *  Android Weather Notification.
 *  Copyright (C) 2011  Denis Nelubin aka Gelin
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
import android.content.res.Resources;
import android.util.Log;
import ru.gelin.android.weather.notification.skin.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 *  Retrieves the integers IDs of resources by the name.
 *  Caches the results.
 *  Use it in the base classes which will be used in multiple applications when you
 *  retrieves a resource by ID.
 */
public class ResourceIdFactory {
    
    /** "id" resource type */
    public static final String ID = "id";
    /** "string" resource type */
    public static final String STRING = "string";
    /** "layout" resource type */
    public static final String LAYOUT = "layout";
    /** "xml" resource type */
    public static final String XML = "xml";
    /** "drawable" resource type */
    public static final String DRAWABLE = "drawable";
    
    /** Map of instances */
    static final Map<String, ResourceIdFactory> instances = new HashMap<String, ResourceIdFactory>();
    
    /** Map of IDs */
    final Map<String, Integer> ids = new HashMap<String, Integer>();
    
    /** Package name for the factory */
    String packageName;
    
    /** Resources */
    Resources resources;
    
    /**
     *  Returns the instance of the factory for the context.
     */
    public static ResourceIdFactory getInstance(Context context) {
        String packageName = context.getPackageName();
        ResourceIdFactory factory = instances.get(packageName);
        if (factory == null) {
            factory = new ResourceIdFactory(context);
            instances.put(packageName, factory);
        }
        return factory;
    }
    
    /**
     *  Private constructor to avoid accidental creation.
     */
    private ResourceIdFactory(Context context) {
        this.packageName = context.getPackageName();
        this.resources = context.getResources();
    }
    
    /**
     *  Returns the ID of the resource with specified type;
     */
    public int id(String type, String name) {
        String key = type + "/" + name;
        Integer id = this.ids.get(key);
        if (id == null) {
            id = this.resources.getIdentifier(name, type, this.packageName);
            this.ids.put(key, id);
        }
        if (id == 0) {
            Log.w(Tag.TAG, this.packageName + ":" + type + "/" + name + " not found");
        }
        return id;
    }
    
    /**
     *  Returns the "id/<name>" resource ID.
     */
    public int id(String name) {
        return id(ID, name);
    }

}
