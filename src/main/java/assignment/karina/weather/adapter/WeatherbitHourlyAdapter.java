package assignment.karina.weather.adapter;

import assignment.karina.weather.adapter.client.Client2;
import assignment.karina.weather.adapter.dto.WeatherbitData;
import assignment.karina.weather.base.Weather;
import assignment.karina.weather.factory.Factory;
import assignment.karina.weather.factory.HourlyFactory;
import assignment.karina.weather.strategy.FetchStrategyMultiple;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherbitHourlyAdapter implements FetchStrategyMultiple {

    private final Client2 client;
    private final Factory factory = new HourlyFactory();

    public WeatherbitHourlyAdapter(Client2 client) {
        this.client = client;
    }

    @Override
    public String name() {
        return "Fetch hourly forecast using Weatherbit API";
    }

    public List<Weather> fetch(String city) {
        WeatherbitData wb = client.fetch(city);
        if (wb == null || wb.data == null || wb.data.isEmpty()) return List.of();

        List<Weather> result = new ArrayList<>(wb.data.size());
        for (WeatherbitData.Data d : wb.data) {
            result.add(factory.createWeather(
                    wb.city,
                    (int) Math.round(d.actuallyTemp),
                    (int) Math.round(d.windSpeed),
                    (int) Math.round(d.pressure),
                    d.humidity
            ));
        }
        return result;
    }
}
