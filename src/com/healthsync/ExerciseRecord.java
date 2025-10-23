package com.healthsync;

import java.time.LocalDate;

public class ExerciseRecord extends TrackableItem {
    private final String exerciseName;
    private final int exerciseTime;
    private final int calories;

    @Override
    public String getSummary() {
        return "🏋🏻 운동 기록 - 날짜: " + getDate() +
                ", 소모 칼로리: " + calories + "kcal, 운동 시간: " + exerciseTime + "분, " +
                "운동명: " + exerciseName + ", 메모: " + getMemo();
    }
    public ExerciseRecord(LocalDate date, String memo, String exerciseName, int exerciseTime, int calories) {
        super(date,memo);
        this.exerciseName = exerciseName;
        this.exerciseTime = exerciseTime;
        this.calories = calories;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public int getExerciseTime() {
        return exerciseTime;
    }

    public int getCalories() {
        return calories;
    }
}
