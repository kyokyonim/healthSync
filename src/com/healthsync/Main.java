package com.healthsync;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LocalDate date = null;

        while(true){
            System.out.println("🔸Please enter the date of birth ( YYYY-MM-DD ): ");
             String input = sc.nextLine();

            try {
                date = LocalDate.parse(input);
                break;
        } catch (DateTimeParseException e) {
                System.out.println("❌ 형식이 올바르지 않습니다. 다시 입력해주세요."); }
        }
        //CRUD 중 사용자에게 입력받아 선택된 것 실행!
        while(true){
            System.out.println("[1. 운동] , [2. 수면] 기록 중 어떤 유형 기록을 시작할까요? (입력 예 : 운동)");
            String input = sc.nextLine();
            if(input.equals("운동")) { System.out.println("운동 선택됨. 기록을 추가/수정/삭제/조회 중 어떤 걸 원하세요?");
                input = sc.nextLine();
                try {
                    if(input.equals("추가")) {   }
                        break;
                } catch(Exception e){System.out.println("다시 입력해주세요");}

        HealthSyncService service = new HealthSyncService();

        System.out.println("==================================================");
        System.out.println("             🚀 HealthSync App 테스트 시작 🚀");
        System.out.println("==================================================");

        System.out.println("\n--- 1. 기록 추가 (addRecord) ---");

        LocalDate yesterday = LocalDate.of(2025,10,20);
        ExerciseRecord workoutYesterday = new ExerciseRecord(
                yesterday, "오후 10시에 Anna 30min 운동","GrowingAnna Intense workout"
                , 30,250);

        service.addRecord(workoutYesterday);

        SleepRecord sleepYesterday = new SleepRecord(
                yesterday,"어제 졸민정 0.25mg -> 5분 뒤 바로 취침",6
        );
        service.addRecord(sleepYesterday);
        LocalDate today = LocalDate.now();
        ExerciseRecord workoutToday = new ExerciseRecord(
                today, "오늘은 아직 운동 전이죠잉! ","조깅(거짓말) " , 50 , 600
        );
        service.addRecord(workoutToday);

        System.out.println("\n--- 2. 전체 기록 조회 (Alldisplayrecords) ---");
        service.Alldisplayrecords();

        System.out.println("\n--- 3. 특정 날짜 기록 조회 (getRecordsDate) - 2025-10-21 ---");
        List<TrackableItem> todayRecords = service.getRecordsDate(today);
        if(!todayRecords.isEmpty()) {
            System.out.println("🔍 " + today + "의 총 기록 수: " + todayRecords.size());
            for(TrackableItem item : todayRecords) {
                System.out.println("-" + item.getSummary());
            }
        } else { System.out.println("🦋해당 날짜의 기록이 없습니다.🦋");}
            System.out.println("\n--- 4. 단일 기록 조회 (getoneRecordsDate) - 2025-10-20 ---");
            Optional<TrackableItem> oneRecord = service.getoneRecordsDate(yesterday);
            if(oneRecord.isPresent()) {
                System.out.println("✅ 첫 번째 기록: " + oneRecord.get().getSummary());
            } else { System.out.println("🧩해당 날짜의 시간이 없습니다.🧩");}
            System.out.println("\n--- 5. 기록 삭제 (delteRecord) - 어제 수면 기록 삭제 ---");
            service.deleteRecord(sleepYesterday);

            System.out.println("== 전체 기록 조회 ==");
            service.Alldisplayrecords();

            System.out.println("==================================================");
            System.out.println("             ✅ HealthSync App 테스트 완료 ✅");
            System.out.println("==================================================");
        }
        }


