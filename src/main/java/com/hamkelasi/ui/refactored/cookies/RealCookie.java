package com.hamkelasi.ui.refactored.cookies;

import com.sun.net.httpserver.HttpContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

public class RealCookie implements MyCookie {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public RealCookie(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Object get(String key) {
        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(key)).findFirst();
    }

    @Override
    public void add(String key, Object value, int expiredTime) {
        final var cookie = new Cookie(key, value.toString());
        cookie.setMaxAge(expiredTime);
        response.addCookie(cookie);
    }
}
