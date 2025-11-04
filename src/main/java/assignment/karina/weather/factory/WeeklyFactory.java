package assignment.karina.weather.factory;

import assignment.karina.weather.base.Weather;
import assignment.karina.weather.base.WeeklyWeather;

public class WeeklyFactory implements Factory {
    @Override
    public Weather createWeather(String city, int actuallyTemp, int windSpeed, int pressure, int humidity) {
        return new WeeklyWeather(city, actuallyTemp, windSpeed, pressure, humidity);
    }
}
