package assignment.karina.weather.observer;

import assignment.karina.weather.facade.WeatherFacade;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CityChange {
    private final WeatherFacade facade;
    private final ForecastSubject subject = new ForecastSubject();

    public CityChange(WeatherFacade facade) { this.facade = facade; }

    public void subscribe(long userId) {
        Observer obs = new ForecastObserver(userId);
        subject.subscribe(userId, obs);
    }
    public void onCityChanged(long userId, String city) {
        Map<String,Object> data = facade.forecast(city);
        subject.notify(userId, city, data);
    }
}
