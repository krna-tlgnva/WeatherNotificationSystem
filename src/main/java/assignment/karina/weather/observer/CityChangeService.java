package assignment.karina.weather.observer;

import assignment.karina.weather.facade.WeatherFacade;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CityChangeService {
    private final WeatherFacade facade;
    private final ForecastSubject subject = new ForecastSubject();

    public CityChangeService(WeatherFacade facade) { this.facade = facade; }

    public void subscribe(long userId) {
        ForecastObserver obs = new InboxObserver(userId);
        subject.subscribe(userId, obs);
    }
    public void unsubscribe(long userId) { subject.unsubscribe(userId); }
    public void onCityChanged(long userId, String city) {
        Map<String,Object> data = facade.forecast(city);
        subject.notifyOne(userId, city, data);
    }
}
