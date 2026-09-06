package com.airtribe.meditrack.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore<T> {

    private final Map<Long, T> data = new HashMap<>();

    public void save(long id, T entity) {
        data.put(id, entity);
    }

    public T findById(long id) {
        return data.get(id);
    }

    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public void update(long id, T entity) {
        data.put(id, entity);
    }

    public void delete(long id) {
        data.remove(id);
    }

    public boolean exists(long id) {
        return data.containsKey(id);
    }

    public int size() {
        return data.size();
    }
}