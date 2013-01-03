/*
 *  Weather API.
 *  Copyright (C) 2010  Vladimir Kubyshev, Denis Nelubin
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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import ru.gelin.android.weather.UnitSystem;

@SuppressWarnings("deprecation")
class EnglishParserHandler extends ParserHandler {
    
    public EnglishParserHandler(GoogleWeather weather) {
        super(weather);
    }

    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException {
        String data = attributes.getValue("data");
        if ("unit_system".equals(localName)) {
            if (data.equalsIgnoreCase("si")) {
                this.weather.unit = UnitSystem.SI;
            } else {
                this.weather.unit = UnitSystem.US;
            }
        } else if ("current_conditions".equals(localName)) {
            this.state = HandlerState.CURRENT_CONDITIONS;
            addCondition();
        } else if ("forecast_conditions".equals(localName)) {
            switch (state) {
            case CURRENT_CONDITIONS:
                this.state = HandlerState.FIRST_FORECAST;
                break;
            case FIRST_FORECAST:
                this.state = HandlerState.NEXT_FORECAST;
                addCondition();
                break;
            default:
                addCondition();
            }
        } else if ("humidity".equals(localName)) {
            this.humidity.parseText(data);
        } else if ("wind_condition".equals(localName)) {
            this.wind.parseText(data);
        } 
    }

}
