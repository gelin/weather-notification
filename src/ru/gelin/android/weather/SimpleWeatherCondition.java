package ru.gelin.android.weather;

/**
 *  Simple weather condition implementation which just holds
 *  the values.
 */
public class SimpleWeatherCondition implements WeatherCondition {

    String conditionText;
    SimpleTemperature temperature;
    String windText;
    String humidityText;
    
    /**
     *  Sets the condition text.
     */
    public void setConditionText(String text) {
        this.conditionText = text;
    }
    
    /**
     *  Sets the temperature.
     */
    public void setTemperature(SimpleTemperature temp) {
        this.temperature = temp;
    }
    
    /**
     *  Sets the wind text.
     */
    public void setWindText(String text) {
        this.windText = text;
    }
    
    /**
     *  Sets the humidity text.
     */
    public void setHumidityText(String text) {
        this.humidityText = text;
    }
    
    @Override
    public String getConditionText() {
        return this.conditionText;
    }

    @Override
    public Temperature getTemperature() {
        return this.temperature;
    }

    @Override
    public Temperature getTemperature(UnitSystem unit) {
        if (this.temperature == null) {
            return null;
        }
        if (this.temperature.getUnitSystem().equals(unit)) {
            return this.temperature;
        }
        return this.temperature.convert(unit);
    }

    @Override
    public String getWindText() {
        return this.windText;
    }
    
    @Override
    public String getHumidityText() {
        return this.humidityText;
    }

}
