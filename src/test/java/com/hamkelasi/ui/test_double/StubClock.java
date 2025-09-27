package com.hamkelasi.ui.test_double;

import com.hamkelasi.bll.refactored.shared.Clock;

import java.time.LocalDateTime;

public class StubClock implements Clock {
    private LocalDateTime now;

    private StubClock(LocalDateTime now) {
        this.now = now;
    }

    public StubClock() {
        now = LocalDateTime.now();
    }

    public void timeTravelTo(LocalDateTime now) {
        this.now = now;
    }

    @Override
    public LocalDateTime now() {
        return now;
    }
}
