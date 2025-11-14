package com.healthsync;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HealthSyncService {
    private final List<TrackableItem> records = new ArrayList<>();

    public void addRecord(TrackableItem item) {
        records.add(item);
        System.out.println("✅새로운 기록이 추가됐습니다.");
    }
    public void Alldisplayrecords() {
        System.out.println("==== 전체 기록 출력 =====");
        for (TrackableItem item : records) {
            System.out.println(item.getSummary());
        }
        System.out.println("===== end =======\n");
    }
    public boolean deleteRecord(TrackableItem item) {
        boolean removed = records.remove(item);
        if (removed) {
            System.out.println("🗑️ 기록이 삭제되었습니다: " + item.getSummary());
        } else {
            System.out.println("⚠️ 삭제할 기록을 찾을 수 없습니다.");
        }
        return removed;
    }

    public void updateRecord(TrackableItem item) {
        if (records.contains(item)) {
            System.out.println("✏️ 기록이 업데이트되었습니다: " + item.getSummary());
        } else {
            System.out.println("⚠️ 업데이트할 기록을 찾을 수 없습니다.");
        }
    }
    public List<ExerciseRecord> getExcerciseRecords(){
        return records.stream()
                .filter(item -> item instanceof ExerciseRecord)
                .map(item -> (ExerciseRecord) item)
                .collect(Collectors.toList());
    }
    public List<SleepRecord> getSleepRecords(){
        return records.stream()
                .filter(item -> item instanceof SleepRecord)
                .map(item -> (SleepRecord) item)
                .collect(Collectors.toList());
    }
    public List<TrackableItem> getAllRecords() {
        return List.copyOf(records);
    }
    //특정 날짜 기록 조회
    public List<TrackableItem> getRecordsDate(LocalDate targetDate) {
        return records.stream()
                        .filter(item -> item.getDate().equals(targetDate))
                        .toList();
    }
    public int calculateTotalWorkoutTime(){
        return records.stream()
                .filter(item -> item instanceof ExerciseRecord)
                .mapToInt(item -> ((ExerciseRecord) item).getExerciseTime())
                .sum();
    }
    public int calculateTotalSleepTime(){
        return records.stream()
                .filter(item -> item instanceof SleepRecord)
                .mapToInt(item -> ((SleepRecord) item).getSleepTime())
                .sum();
    }
    public Optional<TrackableItem> getoneRecordsDate(LocalDate targetDate) {
        return records.stream()
                    .filter(item->item.getDate().equals(targetDate)).findFirst();
    }
}
