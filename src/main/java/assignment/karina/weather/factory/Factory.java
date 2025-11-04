package assignment.karina.weather.factory;

import assignment.karina.weather.base.Weather;

public interface Factory {
    Weather createWeather(String city, int actuallyTemp, int windSpeed, int pressure, int humidity);
}
