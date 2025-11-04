package assignment.karina.weather.adapter.client;

import assignment.karina.weather.adapter.dto.WeatherbitData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Client2 {
    private final WebClient weatherbitClient;
    private final String weatherbitKey;

    public Client2(
            @Value("${weather.base.hourly}") String weatherbitBase,
            @Value("${weather.api.key}") String weatherbitKey
    ) {
        this.weatherbitKey = weatherbitKey;
        this.weatherbitClient = WebClient.builder().baseUrl(weatherbitBase).build();
    }

    public WeatherbitData fetch(String city) {
        return weatherbitClient.get()
                .uri(u -> u
                        .queryParam("city", city)
                        .queryParam("hours", 24)
                        .queryParam("key", weatherbitKey)
                        .build())
                .retrieve()
                .bodyToMono(WeatherbitData.class)
                .block();
    }


}
