package com.healthsync;

import java.io.Serializable;
import java.util.UUID;
import java.time.LocalDate;

public abstract class TrackableItem implements Serializable { // 👈 Serializable 구현
    private static final long SERIALVERSIONUID = 1L; // 👈 버전 ID (권장)

    private final UUID id; // final 속성은 직렬화 가능
    private LocalDate date;
    private String memo;

    // ... 생성자 및 getter/setter 생략
    public TrackableItem(LocalDate date, String memo) {
        this.id = UUID.randomUUID();
        this.date = date;
        this.memo = memo;
    }
    public abstract String getSummary();
    //Getter로 캡슐화
    public UUID getId() { return id;}
    public LocalDate getDate() { return date;}
    public String getMemo() {return memo;}
    //Setter로 캡슐화
    public void setMemo(String memo) {this.memo = memo;}
    public void setDate(LocalDate date) { this.date = date;}
}

// 하위 클래스(SleepRecord, ExerciseRecord)는 별도로 Serializable을 구현할 필요가 없습니다.
// 부모 클래스가 구현하면 자동 상속됩니다.