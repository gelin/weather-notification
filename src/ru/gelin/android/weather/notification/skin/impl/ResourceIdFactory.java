package ru.gelin.android.weather.notification.skin.impl;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;

/**
 *  Retrieves the integers IDs of resources by the name.
 *  Caches the results.
 *  Use it in the base classes which will be used in multiple applications when you
 *  retrieves a resource by ID.
 */
public class ResourceIdFactory {
    
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
        return id;
    }
    
    /**
     *  Returns the "id/<name>" resource ID.
     */
    public int id(String name) {
        return id("id", name);
    }

}
