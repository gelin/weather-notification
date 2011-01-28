package ru.gelin.android.weather.notification.skin.builtin;

import ru.gelin.android.weather.UnitSystem;

public enum TemperatureUnit {

    C(UnitSystem.SI),
    F(UnitSystem.US),
    CF(UnitSystem.SI),
    FC(UnitSystem.US);
    
    UnitSystem unit;
    
    TemperatureUnit(UnitSystem unit) {
        this.unit = unit;
    }
    
    public UnitSystem getUnitSystem() {
        return this.unit;
    }

}
