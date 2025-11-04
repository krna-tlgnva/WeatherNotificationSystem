package assignment.karina.weather.strategy;

import assignment.karina.weather.base.CurrentWeather;

public interface FetchStrategy {
    CurrentWeather fetch(String city);
    String name();
}
