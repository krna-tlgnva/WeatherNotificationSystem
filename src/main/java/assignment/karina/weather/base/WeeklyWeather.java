package assignment.karina.weather.base;

public class WeeklyWeather implements Weather {
    private final String city;
    private final int actuallyTemp;
    private final int windSpeed;
    private final int pressure;
    private final int humidity;

    public WeeklyWeather(String city, int actuallyTemp, int windSpeed, int pressure, int humidity) {
        this.city = city;
        this.actuallyTemp = actuallyTemp;
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
