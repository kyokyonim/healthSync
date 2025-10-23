package com.healthsync;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HealthSyncService service = new HealthSyncService();

        System.out.println("==================================================");
        System.out.println("             🚀 HealthSync에 오신 것을 환영합니다 🚀");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            System.out.println("\n무엇을 도와드릴까요?");
            System.out.println("1. 기록 추가");
            System.out.println("2. 기록 조회");
            System.out.println("3. 기록 삭제");
            System.out.println("4. 종료");
            System.out.print("메뉴 번호를 입력해주세요: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> handleAddRecord(sc, service);
                case "2" -> handleViewRecords(sc, service);
                case "3" -> handleDeleteRecord(sc, service);
                case "4" -> running = false;
                default -> System.out.println("❌ 올바른 번호를 입력해주세요.");
            }
        }

        System.out.println("👋 이용해 주셔서 감사합니다. 다음에 또 만나요!");
    }

    private static void handleAddRecord(Scanner sc, HealthSyncService service) {
        System.out.println("\n어떤 유형의 기록을 추가할까요?");
        System.out.println("1. 운동");
        System.out.println("2. 수면");
        System.out.print("번호를 입력해주세요: ");

        String typeChoice = sc.nextLine().trim();
        LocalDate date = promptDate(sc, "기록 날짜(YYYY-MM-DD)를 입력해주세요: ");
        String memo = promptMemo(sc, "메모를 입력해주세요 (없다면 엔터): ");

        switch (typeChoice) {
            case "1" -> {
                String exerciseName = promptNonEmpty(sc, "어떤 운동을 하셨나요?: ");
                int exerciseTime = promptPositiveInt(sc, "운동 시간(분)을 입력해주세요: ");
                int calories = promptPositiveInt(sc, "소모 칼로리(kcal)를 입력해주세요: ");
                ExerciseRecord record = new ExerciseRecord(date, memo, exerciseName, exerciseTime, calories);
                service.addRecord(record);
            }
            case "2" -> {
                int sleepTime = promptPositiveInt(sc, "수면 시간(시간 단위)을 입력해주세요: ");
                SleepRecord record = new SleepRecord(date, memo, sleepTime);
                service.addRecord(record);
            }
            default -> System.out.println("❌ 지원하지 않는 유형입니다. 처음부터 다시 시도해주세요.");
        }
    }

    private static void handleViewRecords(Scanner sc, HealthSyncService service) {
        System.out.println("\n어떻게 기록을 확인할까요?");
        System.out.println("1. 전체 기록 보기");
        System.out.println("2. 날짜로 검색");
        System.out.print("번호를 입력해주세요: ");

        String viewChoice = sc.nextLine().trim();
        switch (viewChoice) {
            case "1" -> {
                printRecords(service.getAllRecords());
                System.out.println("총 운동 시간: " + service.calculateTotalWorkoutTime() + "분");
            }
            case "2" -> {
                LocalDate targetDate = promptDate(sc, "조회할 날짜(YYYY-MM-DD)를 입력해주세요: ");
                printRecords(service.getRecordsDate(targetDate));
            }
            default -> System.out.println("❌ 올바른 번호를 입력해주세요.");
        }
    }

    private static void handleDeleteRecord(Scanner sc, HealthSyncService service) {
        List<TrackableItem> records = service.getAllRecords();
        if (records.isEmpty()) {
            System.out.println("삭제할 기록이 없습니다.");
            return;
        }

        System.out.println("\n삭제할 기록을 선택해주세요:");
        for (int i = 0; i < records.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, records.get(i).getSummary());
        }

        int index = promptIntInRange(sc, "번호를 입력해주세요: ", 1, records.size());
        TrackableItem item = records.get(index - 1);
        service.deleteRecord(item);
    }

    private static void printRecords(List<TrackableItem> records) {
        if (records.isEmpty()) {
            System.out.println("📭 조회할 기록이 없습니다.");
            return;
        }

        System.out.println("\n==== 기록 목록 ====");
        for (TrackableItem item : records) {
            System.out.println(item.getSummary());
        }
        System.out.println("====================");
    }

    private static LocalDate promptDate(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("❌ 형식이 올바르지 않습니다. 예시: 2024-05-01");
            }
        }
    }

    private static String promptMemo(Scanner sc, String message) {
        System.out.print(message);
        String input = sc.nextLine().trim();
        return input.isEmpty() ? "메모 없음" : input;
    }

    private static String promptNonEmpty(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("❌ 값을 입력해주세요.");
        }
    }

    private static int promptPositiveInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
                System.out.println("❌ 1 이상의 숫자를 입력해주세요.");
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자만 입력해주세요.");
            }
        }
    }

    private static int promptIntInRange(Scanner sc, String message, int min, int max) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("❌ %d부터 %d 사이의 숫자를 입력해주세요.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자만 입력해주세요.");
            }
        }
    }
}
