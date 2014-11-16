

package ru.gelin.android.weather.source;

import android.content.Context;
import ru.gelin.android.weather.notification.app.DebugSettings;

import java.io.File;
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

    static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
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

    private final File path;
    private final String prefix;

    public DebugDumper(Context context, String prefix) {
        DebugSettings settings = new DebugSettings(context);
        this.path = settings.getDebugDir();
        this.prefix = prefix;
    }

    public void dump(String url, String content) {

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

}
