package assignment.karina.weather.factory;

import assignment.karina.weather.base.HourlyWeather;
import assignment.karina.weather.base.Weather;

public class HourlyFactory implements Factory{
    @Override
    public Weather createWeather(String city, int actuallyTemp, int windSpeed, int pressure, int humidity) {
        return new HourlyWeather(city, actuallyTemp, windSpeed, pressure, humidity);
    }
}
