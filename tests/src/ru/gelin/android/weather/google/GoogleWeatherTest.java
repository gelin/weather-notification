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

package ru.gelin.android.weather.google;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import ru.gelin.android.weather.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.TimeZone;

@SuppressWarnings("deprecation")
public class GoogleWeatherTest  extends InstrumentationTestCase {
    
    AssetManager assets;

    @Override
    protected void setUp() throws Exception {
        this.assets = getInstrumentation().getContext().getAssets();
    }

    public void testXmlParseEn() throws Exception {
        InputStream xml1 = this.assets.open("google_weather_api_en.xml");
        InputStream xml2 = this.assets.open("google_weather_api_en.xml");
        GoogleWeather weather = GoogleWeather.parse(
                new InputStreamReader(xml1, "UTF-8"), new InputStreamReader(xml2, "UTF-8"));
        assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        assertEquals(calendar.getTime(), weather.getTime());
        assertEquals(UnitSystem.US, weather.getUnitSystem());
        //assertEquals(TemperatureUnit.F, weather.getTemperatureUnit());
        assertEquals(4, weather.getConditions().size());
        
        WeatherCondition condition0 = weather.getConditions().get(0);
        assertEquals("Clear", condition0.getConditionText());
        Temperature temp0old = condition0.getTemperature();
        assertEquals(-11, temp0old.getCurrent());
        assertEquals(-10, temp0old.getLow());
        assertEquals(-4, temp0old.getHigh());
        Temperature temp0new = condition0.getTemperature(TemperatureUnit.F);
        assertEquals(-11, temp0new.getCurrent());
        assertEquals(-10, temp0new.getLow());
        assertEquals(-4, temp0new.getHigh());
        assertEquals("Humidity: 66%", condition0.getHumidityText());
        assertEquals("Wind: SW at 2 mph", condition0.getWindText());
        assertEquals(66, condition0.getHumidity().getValue());
        Wind wind = condition0.getWind(WindSpeedUnit.MPH);
        assertEquals(WindDirection.SW, wind.getDirection());
        assertEquals(2, wind.getSpeed());
        assertEquals(WindSpeedUnit.MPH, wind.getSpeedUnit());
        
        WeatherCondition condition1 = weather.getConditions().get(1);
        assertEquals("Snow Showers", condition1.getConditionText());
        Temperature temp1old = condition1.getTemperature();
        assertEquals(7, temp1old.getCurrent());
        assertEquals(-7, temp1old.getLow());
        assertEquals(20, temp1old.getHigh());
        Temperature temp1new = condition1.getTemperature(TemperatureUnit.F);
        assertEquals(7, temp1new.getCurrent());
        assertEquals(-7, temp1new.getLow());
        assertEquals(20, temp1new.getHigh());
        
        WeatherCondition condition2 = weather.getConditions().get(2);
        assertEquals("Partly Sunny", condition2.getConditionText());
        Temperature temp2old = condition2.getTemperature();
        assertEquals(-10, temp2old.getCurrent());
        assertEquals(-14, temp2old.getLow());
        assertEquals(-6, temp2old.getHigh());
        Temperature temp2new = condition2.getTemperature(TemperatureUnit.F);
        assertEquals(-10, temp2new.getCurrent());
        assertEquals(-14, temp2new.getLow());
        assertEquals(-6, temp2new.getHigh());
        
        WeatherCondition condition3 = weather.getConditions().get(3);
        assertEquals("Partly Sunny", condition3.getConditionText());
        Temperature temp3old = condition3.getTemperature();
        assertEquals(-22, temp3old.getCurrent());
        assertEquals(-29, temp3old.getLow());
        assertEquals(-15, temp3old.getHigh());
        Temperature temp3new = condition3.getTemperature(TemperatureUnit.F);
        assertEquals(-22, temp3new.getCurrent());
        assertEquals(-29, temp3new.getLow());
        assertEquals(-15, temp3new.getHigh());
    }
    
    public void testTempConvertUS2SI() throws Exception {
        InputStream xml = this.assets.open("google_weather_api_en.xml");

        GoogleWeather weather = new GoogleWeather();
        GoogleWeatherParser parser = new GoogleWeatherParser(weather);
        parser.parse(new InputStreamReader(xml, "UTF-8"), new ParserHandler(weather));

        WeatherCondition condition0 = weather.getConditions().get(0);
        Temperature temp0old = condition0.getTemperature(UnitSystem.SI);
        assertEquals(-24, temp0old.getCurrent());
        assertEquals(-23, temp0old.getLow());  //(-10 - 32) * 5 / 9
        assertEquals(-20, temp0old.getHigh());  //(-4 - 32) * 5 / 9
        
        Temperature temp0new = condition0.getTemperature(TemperatureUnit.C);
        assertEquals(-24, temp0new.getCurrent());
        assertEquals(-23, temp0new.getLow());  //(-10 - 32) * 5 / 9
        assertEquals(-20, temp0new.getHigh());  //(-4 - 32) * 5 / 9
    }

    public void testXmlParseRu() throws Exception {
        InputStream xmlru = this.assets.open("google_weather_api_ru.xml");
        InputStream xmlus = this.assets.open("google_weather_api_en.xml");
        GoogleWeather weather = GoogleWeather.parse(
                new InputStreamReader(xmlus, "UTF-8"), new InputStreamReader(xmlru, "UTF-8"));
        assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2010, Calendar.DECEMBER, 28, 6, 0, 0);
        assertEquals(calendar.getTime(), weather.getTime());
        assertEquals(UnitSystem.SI, weather.getUnitSystem());
        //assertEquals(TemperatureUnit.C, weather.getTemperatureUnit());
        
        assertEquals(4, weather.getConditions().size());
        
        WeatherCondition condition0 = weather.getConditions().get(0);
        assertEquals("Ясно", condition0.getConditionText());
        Temperature temp0 = condition0.getTemperature();
        assertEquals(TemperatureUnit.C, temp0.getTemperatureUnit());
        assertEquals(-24, temp0.getCurrent());
        assertEquals(-23, temp0.getLow());
        assertEquals(-20, temp0.getHigh());
        assertEquals("Влажность: 66 %", condition0.getHumidityText());
        assertEquals("Ветер: ЮЗ, 1 м/с", condition0.getWindText());
        assertEquals(66, condition0.getHumidity().getValue());
        Wind wind = condition0.getWind(WindSpeedUnit.MPH);
        assertEquals(WindDirection.SW, wind.getDirection());
        assertEquals(2, wind.getSpeed());
        assertEquals(WindSpeedUnit.MPH, wind.getSpeedUnit());
        
        WeatherCondition condition1 = weather.getConditions().get(1);
        assertEquals("Ливневый снег", condition1.getConditionText());
        Temperature temp1 = condition1.getTemperature();
        assertEquals(TemperatureUnit.C, temp1.getTemperatureUnit());
        assertEquals(-14, temp1.getCurrent());
        assertEquals(-21, temp1.getLow());
        assertEquals(-7, temp1.getHigh());
        
        WeatherCondition condition2 = weather.getConditions().get(2);
        assertEquals("Местами солнечно", condition2.getConditionText());
        Temperature temp2 = condition2.getTemperature();
        assertEquals(TemperatureUnit.C, temp2.getTemperatureUnit());
        assertEquals(-23, temp2.getCurrent());
        assertEquals(-26, temp2.getLow());
        assertEquals(-21, temp2.getHigh());
        
        WeatherCondition condition3 = weather.getConditions().get(3);
        assertEquals("Местами солнечно", condition3.getConditionText());
        Temperature temp3 = condition3.getTemperature();
        assertEquals(TemperatureUnit.C, temp3.getTemperatureUnit());
        assertEquals(-30, temp3.getCurrent());
        assertEquals(-34, temp3.getLow());
        assertEquals(-26, temp3.getHigh());
    }
    
    public void testTempConvertSI2US() throws Exception {
        InputStream xml = this.assets.open("google_weather_api_ru.xml");
        GoogleWeather weather = new GoogleWeather();
        GoogleWeatherParser parser = new GoogleWeatherParser(weather);
        parser.parse(new InputStreamReader(xml, "UTF-8"), new ParserHandler(weather));
        WeatherCondition condition0 = weather.getConditions().get(0);
        Temperature temp0 = condition0.getTemperature(UnitSystem.US);
        assertEquals(-11, temp0.getCurrent());
        assertEquals(-9, temp0.getLow());   //-23 * 9 / 5 + 32
        assertEquals(-4, temp0.getHigh());  //-20 * 9 / 5 + 32
    }
    
    public void testTempConvertC2F() throws Exception {
        InputStream xml = this.assets.open("google_weather_api_ru.xml");
        GoogleWeather weather = new GoogleWeather();
        GoogleWeatherParser parser = new GoogleWeatherParser(weather);
        parser.parse(new InputStreamReader(xml, "UTF-8"), new ParserHandler(weather));
        WeatherCondition condition0 = weather.getConditions().get(0);
        Temperature temp0 = condition0.getTemperature(TemperatureUnit.F);
        assertEquals(-11, temp0.getCurrent());
        assertEquals(-9, temp0.getLow());   //-23 * 9 / 5 + 32
        assertEquals(-4, temp0.getHigh());  //-20 * 9 / 5 + 32
    }

    public void testTempConvertMPH2MPSKMPS() throws Exception {
        InputStream xmlru = this.assets.open("google_weather_api_ru.xml");
        InputStream xmlus = this.assets.open("google_weather_api_en.xml");
        GoogleWeather weather = GoogleWeather.parse(
                new InputStreamReader(xmlus, "UTF-8"), new InputStreamReader(xmlru, "UTF-8"));
        WeatherCondition condition0 = weather.getConditions().get(0);
        Wind windKMPH = condition0.getWind(WindSpeedUnit.KMPH);
        assertEquals(3, windKMPH.getSpeed()); //2 mph * 1.6
        Wind windMPS = condition0.getWind(WindSpeedUnit.MPS);
        assertEquals(1, windMPS.getSpeed()); //2 mph * 0.44
    }
    
    public void testUnknownWeather() throws Exception {
        InputStream xmlun = this.assets.open("google_weather_api_ru_2011-03.xml");
        InputStream xmlus = this.assets.open("google_weather_api_en.xml");
        GoogleWeather weather = GoogleWeather.parse(
                new InputStreamReader(xmlus, "UTF-8"), new InputStreamReader(xmlun, "UTF-8"));
        assertFalse(weather.isEmpty());
        
        assertEquals("Omsk, Omsk Oblast", weather.getLocation().getText());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2011, Calendar.MARCH, 22, 0, 0, 0);
        assertEquals(calendar.getTime(), weather.getTime());
        assertEquals(UnitSystem.SI, weather.getUnitSystem());
        //assertEquals(TemperatureUnit.C, weather.getTemperatureUnit());
        
        assertEquals(4, weather.getConditions().size());
        
        WeatherCondition condition0 = weather.getConditions().get(0);
        assertEquals("Преимущественно облачно", condition0.getConditionText());
        Temperature temp0 = condition0.getTemperature();
        assertEquals(TemperatureUnit.C, temp0.getTemperatureUnit());
        assertEquals(-5, temp0.getCurrent());
        assertEquals(-9, temp0.getLow());
        assertEquals(-1, temp0.getHigh());
        assertEquals("Влажность: 83 %", condition0.getHumidityText());
        assertEquals("Ветер: Ю, 4 м/с", condition0.getWindText());
        assertEquals(66, condition0.getHumidity().getValue());  //value from EN XML
        Wind wind = condition0.getWind(WindSpeedUnit.MPH);
        assertEquals(WindDirection.SW, wind.getDirection());  //value from EN XML
        assertEquals(2, wind.getSpeed());   //value from EN XML
        assertEquals(WindSpeedUnit.MPH, wind.getSpeedUnit());
        
        WeatherCondition condition1 = weather.getConditions().get(1);
        assertEquals("Переменная облачность", condition1.getConditionText());
        Temperature temp1 = condition1.getTemperature();
        assertEquals(-7, temp1.getLow());
        assertEquals(-2, temp1.getHigh());
        
        WeatherCondition condition2 = weather.getConditions().get(2);
        assertEquals("Преимущественно облачно", condition2.getConditionText());
        Temperature temp2 = condition2.getTemperature();
        assertEquals(-7, temp2.getLow());
        assertEquals(3, temp2.getHigh());
        
        WeatherCondition condition3 = weather.getConditions().get(3);
        assertEquals("Ливневый снег", condition3.getConditionText());
        Temperature temp3 = condition3.getTemperature();
        assertEquals(-3, temp3.getLow());
        assertEquals(2, temp3.getHigh());
    }

}
