package assignment.karina.weather.strategy;

import assignment.karina.weather.base.Weather;
import java.util.List;

public interface FetchStrategyMultiple {
    List<Weather> fetch(String city);
    String name();
}
