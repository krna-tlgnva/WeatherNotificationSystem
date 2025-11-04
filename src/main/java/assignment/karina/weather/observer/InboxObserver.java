package assignment.karina.weather.observer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public record InboxObserver(long userId) implements ForecastObserver {
    private static final Map<Long, Deque<Map<String, Object>>> BOX = new ConcurrentHashMap<>();

    @Override
    public void onForecast(String city, Map<String, Object> data) {
        var q = BOX.computeIfAbsent(userId, k -> new ArrayDeque<>());
        var item = new LinkedHashMap<String, Object>();
        item.put("ts", System.currentTimeMillis());
        item.put("city", city);
        item.put("data", data);
        q.offerFirst(item);
        while (q.size() > 20) q.removeLast();
    }

    public static List<Map<String, Object>> inbox(long userId) {
        return new ArrayList<>(BOX.getOrDefault(userId, new ArrayDeque<>()));
    }
}
