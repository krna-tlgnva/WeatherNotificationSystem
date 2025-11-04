package assignment.karina.weather.adapter.client;

import assignment.karina.weather.adapter.dto.OpenWeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Client1 {
    private final WebClient openWeatherClient;
    private final String openWeatherKey;

    public Client1(
            @Value("${openweather.api.base}") String openWeatherBase,
            @Value("${openweather.api.key}") String openWeatherKey
    ) {
        this.openWeatherKey = openWeatherKey;
        this.openWeatherClient = WebClient.builder().baseUrl(openWeatherBase).build();
    }

    public OpenWeatherData fetch(String city) {
        return openWeatherClient.get()
                .uri(uri -> uri.path("/data/2.5/weather")
                        .queryParam("q", city)
                        .queryParam("appid", openWeatherKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(OpenWeatherData.class)
                .block();
    }
}
