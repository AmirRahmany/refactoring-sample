package com.hamkelasi.bll;


import com.ibm.icu.util.PersianCalendar;

import java.time.LocalDateTime;
import java.util.Date;

public class IranianCalendar {
    private PersianCalendar persianCalendar;
    private String persianDate;

    public IranianCalendar(Date date) {
        persianCalendar = new PersianCalendar();
        persianCalendar.setTime(date); // Set the calendar's time to the input date
        int year = persianCalendar.get(PersianCalendar.YEAR);
        int month = persianCalendar.get(PersianCalendar.MONTH) + 1; // ICU4J months are 0-based
        int day = persianCalendar.get(PersianCalendar.DAY_OF_MONTH);

        persianDate = year + "/" + month + "/" + day;
    }

    public IranianCalendar() {
        persianCalendar = new PersianCalendar();
    }

    public IranianCalendar(LocalDateTime registerDate) {

    }

    public Date convertToDateTime(int year, int month, int day) {
        PersianCalendar calendar = new PersianCalendar();
        calendar.set(year, month - 1, day); // ICU4J months are 0-based
        return calendar.getTime();
    }

    @Override
    public String toString() {
        return persianDate != null ? persianDate : "";
    }
}
