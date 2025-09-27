package com.hamkelasi.ui.refactored.cookies;

import java.time.LocalDateTime;

public interface MyCookie {

    Object get(String key);
    void add(String key, Object value, LocalDateTime expiredTime);
}
