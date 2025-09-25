package com.hamkelasi.ui.refactored.session_management;

public interface Session {
    void set(String key,Object value);
    Object get(String key);
}
