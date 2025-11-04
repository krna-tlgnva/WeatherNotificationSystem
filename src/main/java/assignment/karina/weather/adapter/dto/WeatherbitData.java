package assignment.karina.weather.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class WeatherbitData {
    @JsonProperty("city_name")
    public String city;

    @JsonProperty("data")
    public List<Data> data;

    public static class Data {
        @JsonProperty("temp")     public double actuallyTemp;
        @JsonProperty("app_temp") public double feelsLike;
        @JsonProperty("pres")     public double pressure;
        @JsonProperty("rh")       public int humidity;
        @JsonProperty("wind_spd") public double windSpeed;
    }
}
