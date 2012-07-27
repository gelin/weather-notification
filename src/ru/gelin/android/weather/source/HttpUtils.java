package ru.gelin.android.weather.source;

import org.apache.http.HttpEntity;

/**
 *  Some utilities to work with HTTP requests.
 */
public class HttpUtils {

    private HttpUtils() {
        //avoid instatiation
    }

    public static String getCharset(HttpEntity entity) {
        return getCharset(entity.getContentType().toString());
    }

    static String getCharset(String contentType) {
        if (contentType == null) {
            return HttpWeatherSource.ENCODING;
        }
        int charsetPos = contentType.indexOf(HttpWeatherSource.CHARSET);
        if (charsetPos < 0) {
            return HttpWeatherSource.ENCODING;
        }
        charsetPos += HttpWeatherSource.CHARSET.length();
        int endPos = contentType.indexOf(';', charsetPos);
        if (endPos < 0) {
            endPos = contentType.length();
        }
        return contentType.substring(charsetPos, endPos);
    }
}
