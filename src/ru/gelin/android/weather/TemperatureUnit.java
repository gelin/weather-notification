package ru.gelin.android.weather;

public enum TemperatureUnit {
    C, F;
    //K

    /**
     *  Converts deprecated UnitSystem to new TemperatureUnit.
     */
    @SuppressWarnings("deprecation")
    public static TemperatureUnit valueOf(UnitSystem unitSystem) {
        switch (unitSystem) {
        case SI: 
            return TemperatureUnit.C;
        case US: 
            return TemperatureUnit.F;
        }
        return TemperatureUnit.C;
    }

}
