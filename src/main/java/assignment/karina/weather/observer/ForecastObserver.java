package assignment.karina.weather.observer;

import java.util.Map;

public interface ForecastObserver {
    void onForecast(String city, Map<String, Object> data);
}

