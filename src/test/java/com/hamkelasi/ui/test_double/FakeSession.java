package com.hamkelasi.ui.test_double;

import com.hamkelasi.ui.refactored.session_management.Session;

import java.util.LinkedHashMap;
import java.util.Map;

public class FakeSession implements Session {
    private final Map<String,Object> sessionStorages = new LinkedHashMap<>();
    @Override
    public void set(String key, Object value) {
        sessionStorages.put(key, value);
    }

    @Override
    public Object get(String key) {
        return sessionStorages.get(key);
    }
}
