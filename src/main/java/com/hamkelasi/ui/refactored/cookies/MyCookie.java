package com.hamkelasi.ui.refactored.cookies;

public interface MyCookie {

    Object get(String key);
    void add(String key,Object value,int expiredTime);
}
