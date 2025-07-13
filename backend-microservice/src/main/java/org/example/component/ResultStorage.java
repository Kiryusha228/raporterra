package org.example.component;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ResultStorage {
    private final ConcurrentMap<Long, List<Map<String, Object>>> resultMap = new ConcurrentHashMap<>();

    public void putResult(Long taskId, List<Map<String, Object>> result) {
        resultMap.put(taskId, result);
    }

    public List<Map<String, Object>> getResult(Long taskId) {
        return resultMap.get(taskId);
    }

    public void remove(Long taskId) {
        resultMap.remove(taskId);
    }
}
