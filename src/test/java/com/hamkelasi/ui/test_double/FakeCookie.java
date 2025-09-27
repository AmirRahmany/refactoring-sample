package com.hamkelasi.ui.test_double;

import com.hamkelasi.ui.refactored.cookies.MyCookie;
import org.antlr.v4.runtime.misc.Triple;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class FakeCookie implements MyCookie {
    private final List<Triple<String, String, Integer>> cookies = new ArrayList<>();

    @Override
    public void add(String key, Object value, LocalDateTime expiredTime) {
        cookies.add(new Triple<>(key, String.valueOf(value), expiredTime.getSecond()));
    }

    @Override
    public Object get(String key) {
        final Optional<Triple<String, String, Integer>> result = cookies.stream().filter(cookie -> cookie.a.equals(key)).findFirst();
        if (result.isEmpty()) return null;
        return result.get().b;
    }

    public int getExpireDateOf(String key) {
        final Optional<Triple<String, String, Integer>> result = cookies.stream().filter(cookie -> cookie.a.equals(key)).findFirst();
        if (result.isEmpty()) return 0;
        return result.get().c;
    }
}
