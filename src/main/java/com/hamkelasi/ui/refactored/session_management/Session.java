package com.hamkelasi.ui.refactored.session_management;

public interface Session {
    void set(String key,Object obj);
    Object get(String key);
}
