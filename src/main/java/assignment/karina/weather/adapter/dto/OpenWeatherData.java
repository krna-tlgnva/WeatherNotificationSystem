package assignment.karina.weather.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenWeatherData {
    public Main main;
    public Wind wind;

    @JsonProperty("name")
    public String city;

    public static class Main {
        @JsonProperty("temp")
        public int actuallyTemp;
        @JsonProperty("feels_like")
        public int feelsLike;
        public int pressure;
        public int humidity;
    }

    public static class Wind {
        @JsonProperty("speed")
        public int windSpeed;
    }
}
