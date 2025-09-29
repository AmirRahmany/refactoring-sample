package com.hamkelasi.bll.utils;

import java.time.format.DateTimeFormatter;

public class TimeUtils {
    public static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
    }
}