package assignment.karina.weather.observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ForecastSubject {
    private final ConcurrentHashMap<Long, Observer> observers = new ConcurrentHashMap<>();
    public void subscribe(long userId, Observer observer) {
        observers.put(userId, observer);
    }
    public void notify(long userId, String city, Map<String, Object> data) {
        Observer observer = observers.get(userId);
        if (observer != null) {
            observer.onForecast(city, data);
        }
    }
}
