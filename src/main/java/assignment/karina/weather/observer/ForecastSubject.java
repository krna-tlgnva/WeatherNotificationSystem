package assignment.karina.weather.observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ForecastSubject {
    private final ConcurrentHashMap<Long, ForecastObserver> observers = new ConcurrentHashMap<>();

    public void subscribe(long userId, ForecastObserver o){ observers.put(userId, o); }
    public void unsubscribe(long userId){ observers.remove(userId); }

    public void notifyOne(long userId, String city, Map<String,Object> data) {
        var o = observers.get(userId);
        if (o != null) o.onForecast(city, data);
    }
}
