package com.hamkelasi.ui.test_double;

import com.hamkelasi.ui.refactored.cookies.MyCookie;

import java.util.LinkedHashMap;
import java.util.Map;

public class FakeCookie implements MyCookie {
    private final Map<String,String> cookies = new LinkedHashMap<>();
    @Override
    public Object get(String key) {
        return cookies.get(key);
    }

    @Override
    public void add(String key, Object value, int expiredTime) {
        cookies.put(key,value.toString());
    }
}
