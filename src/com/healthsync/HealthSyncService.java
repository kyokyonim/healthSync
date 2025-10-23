package com.healthsync;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HealthSyncService {
    List<TrackableItem> records = new ArrayList<>();

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
    public void deleteRecord(TrackableItem item) {
        records.remove(item);
        System.out.println(item + "이 삭제 되었습니다.");
    }
    public void updateRecord(TrackableItem item) {
        records.set(records.indexOf(item), item);
    }
    public List<ExerciseRecord> getExcerciseRecords(){
        return records.stream()
                .filter(item -> item instanceof ExerciseRecord)
                .map(item -> (ExerciseRecord) item)
                .collect(Collectors.toList());
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
                .mapToInt(item -> ((ExerciseRecord) item).exerciseTime)
                .sum();
    }
    public Optional<TrackableItem> getoneRecordsDate(LocalDate targetDate) {
        return records.stream()
                    .filter(item->item.getDate().equals(targetDate)).findFirst();
    }
}
