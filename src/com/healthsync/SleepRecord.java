package com.healthsync;

import java.time.LocalDate;

public class SleepRecord extends TrackableItem {
    public int sleeptime;


    public String getSummary(){
        return "🛌 수면 기록 - 날짜: " + getDate() +
                ", 수면 시간: " + sleeptime + "시간, 메모: " + getMemo();
    }
    public SleepRecord(LocalDate date, String memo, int sleeptime) {
        super(date, memo);
        this.sleeptime = sleeptime;
    }



}
