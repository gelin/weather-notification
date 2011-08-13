package ru.gelin.android.weather.notification.skin.impl;

import ru.gelin.android.weather.WindSpeedUnit;

//TODO: Remove?
public enum WindUnit {
	MPH(WindSpeedUnit.MPH),
    MPS(WindSpeedUnit.MPS),
    KMPH(WindSpeedUnit.KMPH);
    
    WindSpeedUnit unit;
    
	WindUnit(WindSpeedUnit unit) {
        this.unit = unit;
    }
    
    public WindSpeedUnit getWindSpeedUnit() {
        return this.unit;
    }
}
