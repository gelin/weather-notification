/*
 *  Weather API.
 *  Copyright (C) 2011  Denis Nelubin, Vladimir Kubyshev
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
 */

package ru.gelin.android.weather.google;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.gelin.android.weather.Weather;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.Reader;

class GoogleWeatherParser {

    GoogleWeather weather;

    public GoogleWeatherParser(Weather weather) {
        this.weather = (GoogleWeather)weather;
    }

    //@Override
    public void parse(Reader xml, DefaultHandler handler) 
            throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);   //WTF??? Harmony's Expat is so...
        //factory.setFeature("http://xml.org/sax/features/namespaces", false);
        //factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        SAXParser parser = factory.newSAXParser();
        //explicitly decoding from UTF-8 because Google misses encoding in XML preamble
        parser.parse(new InputSource(xml), handler);
    }

}
