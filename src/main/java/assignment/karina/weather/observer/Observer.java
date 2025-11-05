package assignment.karina.weather.observer;

import java.util.Map;

public interface Observer {
    void onForecast(String city, Map<String, Object> data);
}

