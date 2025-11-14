package com.healthsync;

import java.time.LocalDate;

public class SleepRecord extends TrackableItem {
    private int sleepTime;

    public SleepRecord(LocalDate date, String memo, int sleeptime) {
        super(date, memo);
        this.sleepTime = sleeptime;
    }

    public String getSummary() {
        return "🛌 수면 기록 - 날짜: " + getDate() +
                ", 수면 시간: " + sleepTime + "시간, 메모: " + getMemo();
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }
}
