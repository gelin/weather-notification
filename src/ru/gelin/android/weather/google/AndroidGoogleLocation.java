package ru.gelin.android.weather.google;

import android.location.Address;
import ru.gelin.android.weather.Location;

/**
 *  Wrapper for Android location to query Google weather API with geo coordinates.
 */
public class AndroidGoogleLocation implements Location {

    /** Query template */
    static final String QUERY = "%s,%s,%s,%d,%d";
    
    /** Android location */
    android.location.Location location;
    /** Android address */
    Address address;
    
    /**
     *  Creates the location from Android location.
     */
    public AndroidGoogleLocation(android.location.Location location) {
        this.location = location;
    }
    
    /**
     *  Creates the location from Android location and address.
     */
    public AndroidGoogleLocation(android.location.Location location, Address address) {
        this.location = location;
        this.address = address;
    }
    
    /**
     *  Creates the query with geo coordinates. 
     *  For example: ",,,30670000,104019996"
     */
    @Override
    public String getQuery() {
        if (location == null) {
            return "";
        }
        if (address == null) {
            return String.format(QUERY,
                    "", "", "",
                    convertGeo(location.getLatitude()),
                    convertGeo(location.getLongitude()));
        }
        return String.format(QUERY,
                stringOrEmpty(address.getLocality()),
                stringOrEmpty(address.getAdminArea()),
                stringOrEmpty(address.getCountryName()),
                convertGeo(location.getLatitude()),
                convertGeo(location.getLongitude()));
    }

    @Override
    public String getText() {
        if (location == null) {
            return "";
        }
        if (address == null) {
            return android.location.Location.convert(location.getLatitude(), 
                    android.location.Location.FORMAT_DEGREES) +
                    " " +
                    android.location.Location.convert(location.getLongitude(), 
                            android.location.Location.FORMAT_DEGREES);
        }
        StringBuilder result = new StringBuilder();
        result.append(stringOrEmpty(address.getLocality()));
        if (result.length() > 0) {
            result.append(", ");
        }
        result.append(stringOrEmpty(address.getAdminArea()));
        if (result.length() == 0) {
            result.append(stringOrEmpty(address.getCountryName()));
        }
        return result.toString();
    }
    
    int convertGeo(double geo) {
        return (int)(geo * 1000000);
    }
    
    String stringOrEmpty(String string) {
        if (string == null) {
            return "";
        }
        return string;
    }

}
