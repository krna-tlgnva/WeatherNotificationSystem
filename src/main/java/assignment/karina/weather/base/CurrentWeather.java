package assignment.karina.weather.base;

public class CurrentWeather implements Weather {
    private final String city;
    private final int actuallyTemp;
    private final int feelsLike;
    private final int windSpeed;
    private final int pressure;
    private final int humidity;

    public CurrentWeather(String city, int actuallyTemp, int feelsLike, int windSpeed, int pressure, int humidity) {
        this.city = city;
        this.actuallyTemp = actuallyTemp;
        this.feelsLike = feelsLike;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public String getCity() {
        return city;
    }
    public int getActuallyTemp() {
        return actuallyTemp;
    }
    public int getFeelsLike() {
        return feelsLike;
    }
    public int getWindSpeed() {
        return windSpeed;
    }
    public int getPressure() {
        return pressure;
    }
    public int getHumidity() {
        return humidity;
    }
}
