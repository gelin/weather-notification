package ru.gelin.android.weather.google;

import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.gelin.android.weather.WeatherParser;
import ru.gelin.android.weather.Weather;

public class GoogleWeatherParser implements WeatherParser{

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
