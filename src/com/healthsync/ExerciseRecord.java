package com.healthsync;

import java.time.LocalDate;

public class ExerciseRecord extends TrackableItem {
    public String exerciseName;
    public int exerciseTime;
    public int calories;

    @Override
    public String getSummary() {
        return "🏋🏻 운동 기록 - 날짜: " + getDate() +
          " 소모 칼로리 : " + calories + " 운동 시간 : " + exerciseTime +
                " 메모: " + getMemo() + exerciseName;
    }
    public ExerciseRecord(LocalDate date, String memo, String exerciseName, int exerciseTime, int calories) {
        super(date,memo);
        this.exerciseName = exerciseName;
        this.exerciseTime = exerciseTime;
        this.calories = calories;
    }
}
