package assignment.karina.weather.builder;

import assignment.karina.weather.base.CurrentWeather;

public class Builder {
    private String city;
    private int actuallyTemp;
    private int feelsLike;
    private int windSpeed;
    private int pressure;
    private int humidity;

    public Builder withCity(String city) {
        this.city = city;
        return this;
    }

    public Builder withActuallyTemp(int actuallyTemp) {
        this.actuallyTemp = actuallyTemp;
        return this;
    }

    public Builder withFeelsLike(int feelsLike) {
        this.feelsLike = feelsLike;
        return this;
    }

    public Builder withWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
        return this;
    }

    public Builder withPressure(int pressure) {
        this.pressure = pressure;
        return this;
    }

    public Builder withHumidity(int humidity) {
        this.humidity = humidity;
        return this;
    }


    public CurrentWeather build() {
        return new CurrentWeather(city,actuallyTemp, feelsLike, windSpeed, pressure, humidity);
    }

}
