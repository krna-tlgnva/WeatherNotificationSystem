package assignment.karina.weather.adapter;

import assignment.karina.weather.adapter.client.Client1;
import assignment.karina.weather.adapter.dto.OpenWeatherData;
import assignment.karina.weather.base.CurrentWeather;
import assignment.karina.weather.builder.Builder;
import assignment.karina.weather.strategy.FetchStrategy;
import org.springframework.stereotype.Component;

@Component("apiFetchStrategy")
public record OpenWeatherAdapter(Client1 client) implements FetchStrategy {
    @Override
    public String name() {
        return "Fetch data by using api 1";
    }

    @Override
    public CurrentWeather fetch(String city) {
        OpenWeatherData pieceOfData = client.fetch(city);
        return new Builder()
                .withCity(pieceOfData.city)
                .withActuallyTemp(pieceOfData.main.actuallyTemp)
                .withFeelsLike(pieceOfData.main.feelsLike)
                .withWindSpeed(pieceOfData.wind.windSpeed)
                .withHumidity(pieceOfData.main.humidity)
                .withPressure(pieceOfData.main.pressure)
                .build();
    }
}
