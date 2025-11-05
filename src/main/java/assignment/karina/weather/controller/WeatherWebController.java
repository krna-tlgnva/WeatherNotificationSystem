package assignment.karina.weather.controller;

import assignment.karina.weather.facade.WeatherFacade;
import assignment.karina.weather.observer.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class WeatherWebController {

    private final WeatherFacade weatherService;
    private final CityChange cityChangeService;

    public WeatherWebController(WeatherFacade weatherService, CityChange cityChangeService) {
        this.weatherService = weatherService;
        this.cityChangeService = cityChangeService;
    }

    @ResponseBody
    @GetMapping("/api/weather/{city}")
    public Map<String, Object> getWeather(@PathVariable String city) {
        return weatherService.forecast(city);
    }

    @ResponseBody
    @PostMapping("/api/observer/subscribe")
    public Map<String, Boolean> subscribe(@RequestBody Map<String, String> body) {
        long userId = Long.parseLong(body.getOrDefault("userId", "1"));
        cityChangeService.subscribe(userId);
        return Map.of("ok", true);
    }

    @ResponseBody
    @PostMapping("/api/observer/city-changed")
    public Map<String, Boolean> cityChanged(@RequestBody Map<String, String> body) {
        long userId = Long.parseLong(body.getOrDefault("userId", "1"));
        String city = body.getOrDefault("city", "Astana");
        cityChangeService.onCityChanged(userId, city);
        return Map.of("ok", true);
    }

    @ResponseBody
    @GetMapping("/api/observer/inbox")
    public ResponseEntity<List<Map<String, Object>>> inbox(@RequestParam long userId) {
        return ResponseEntity.ok(ForecastObserver.inbox(userId));
    }
}
