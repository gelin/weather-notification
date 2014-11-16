

package ru.gelin.android.weather.source;

import android.content.Context;
import android.util.Log;
import ru.gelin.android.weather.notification.app.DebugSettings;
import ru.gelin.android.weather.notification.app.Tag;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
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

    static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss'Z'");
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

    private final DebugSettings settings;
    private final File path;
    private final String prefix;

    public DebugDumper(Context context, String prefix) {
        this.settings = new DebugSettings(context);
        this.path = settings.getDebugDir();
        this.prefix = prefix;
    }

    public void dump(String url, String content) {
        if (!this.settings.isAPIDebug()) {
            return;
        }
        File dumpFile = getDumpFile(url);
        File dir = dumpFile.getParentFile();
        if (!(dir.mkdirs() || dir.isDirectory())) {
            Log.w(Tag.TAG, "cannot create dir " + dir);
            return;
        }
        try {
            Log.d(Tag.TAG, "dumping to " + dumpFile);
            Writer out = new FileWriter(dumpFile);
            out.write(content);
            out.close();
        } catch (Exception e) {
            Log.w(Tag.TAG, "cannot create debug dump", e);
        }
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
