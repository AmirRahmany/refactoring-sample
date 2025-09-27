package com.hamkelasi.bll.refactored.shared;

import java.time.LocalDateTime;

public class SystemClock implements Clock {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
