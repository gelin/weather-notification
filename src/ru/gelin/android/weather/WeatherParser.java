package ru.gelin.android.weather;

import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public interface WeatherParser {

	public void parse(Reader xml, DefaultHandler handler) throws SAXException, ParserConfigurationException, IOException; 
}
