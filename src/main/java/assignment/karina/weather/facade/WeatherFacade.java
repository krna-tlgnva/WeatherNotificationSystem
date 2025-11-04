package assignment.karina.weather.facade;

import assignment.karina.weather.adapter.OpenWeatherAdapter;
import assignment.karina.weather.adapter.WeatherbitHourlyAdapter;
import assignment.karina.weather.adapter.WeatherbitWeeklyAdapter;
import assignment.karina.weather.base.CurrentWeather;
import assignment.karina.weather.strategy.FetchStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeatherFacade {
    private final OpenWeatherAdapter openWeather;
    private final WeatherbitHourlyAdapter wbHourly;
    private final WeatherbitWeeklyAdapter wbWeekly;

    public WeatherFacade(OpenWeatherAdapter openWeather,
                         WeatherbitHourlyAdapter wbHourly,
                         WeatherbitWeeklyAdapter wbWeekly) {
        this.openWeather = openWeather;
        this.wbHourly = wbHourly;
        this.wbWeekly = wbWeekly;
    }

    public Map<String, Object> forecast(String city) {
        var current = openWeather.fetch(city);
        var hourly  = wbHourly.fetch(city);
        var weekly  = wbWeekly.fetch(city);

        return Map.of(
                "current", current,
                "hourly",  hourly,
                "weekly",  weekly
        );
    }
}

