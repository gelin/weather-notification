package ru.gelin.android.weather.notification.skin.impl;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import ru.gelin.android.weather.notification.Tag;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *  Parses the layout XML resource to find out all IDs mentioned in it.
 */
public class LayoutIdsDetector {

    private static final String NS = "http://schemas.android.com/apk/res/android";
    private static final String ID = "id";

    private final Context context;
    /** Ids of the layout views. */
    final Set<Integer> ids = new HashSet<Integer>();

    public LayoutIdsDetector(Context context) {
        this.context = context;
    }

    /**
     *  Parses the layout, lists internally all IDs mentioned in the layout XML definition.
     */
    public void parse(int layoutId) {
        XmlResourceParser parser = this.context.getResources().getLayout(layoutId);
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                    String attr = parser.getAttributeValue(NS, ID);
                    if (attr != null) {
                        int id = Integer.parseInt(attr.substring(1));   //string is "@123456"
                        this.ids.add(id);
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            Log.w(Tag.TAG, e);
        } catch (IOException e) {
            Log.w(Tag.TAG, e);
        }
    }

    /**
     *  Checks that the parsed layout contains the specified ID.
     */
    public boolean contains(int id) {
        return this.ids.contains(id);
    }

}
