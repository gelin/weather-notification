package ru.gelin.android.weather.source;

import android.test.AndroidTestCase;

public class HttpUtilsTest extends AndroidTestCase {

    public void testGetCharset() {
        assertEquals("UTF-8", HttpUtils.getCharset("text/xml; charset=UTF-8"));
        assertEquals("windows-1251", HttpUtils.getCharset("text/xml; charset=windows-1251"));
        assertEquals("UTF-8", HttpUtils.getCharset("text/xml"));
        assertEquals("windows-1251", HttpUtils.getCharset("text/xml; charset=windows-1251; something more"));
        assertEquals("UTF-8", HttpUtils.getCharset(""));
        assertEquals("UTF-8", HttpUtils.getCharset((String) null));
    }

}
