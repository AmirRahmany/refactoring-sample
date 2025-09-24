package com.hamkelasi.ui.refactored.session_management;

import jakarta.servlet.http.HttpServletRequest;

public class RealSession implements Session {

    private HttpServletRequest request;

    public RealSession(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void set(String key, Object value) {
        request.getSession().setAttribute(key,value);
    }

    @Override
    public Object get(String key) {
        return request.getSession().getAttribute(key);
    }
}
