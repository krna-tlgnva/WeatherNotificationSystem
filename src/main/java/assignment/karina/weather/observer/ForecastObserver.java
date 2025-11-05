package assignment.karina.weather.observer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public record ForecastObserver(long userId) implements Observer {
    private static final Map<Long, Deque<Map<String, Object>>> storage = new ConcurrentHashMap<>();

    @Override
    public void onForecast(String city, Map<String, Object> data) {
        Deque<Map<String, Object>> deque = storage.computeIfAbsent(userId, k -> new ArrayDeque<>());
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("ts", System.currentTimeMillis());
        item.put("city", city);
        item.put("data", data);
        deque.offerFirst(item);
        while (deque.size() > 5) {
            deque.removeLast();
        }
    }

    public static List<Map<String, Object>> inbox(long userId) {
        return new ArrayList<>(storage.getOrDefault(userId, new ArrayDeque<>()));
    }
}
