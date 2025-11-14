package com.healthsync;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private enum RecordType {
        EXERCISE("운동"),
        SLEEP("수면");

        private final String displayName;

        RecordType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private enum RecordAction {
        ADD,
        UPDATE,
        DELETE,
        VIEW,
        BACK,
        EXIT
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HealthSyncService service = new HealthSyncService();

        System.out.println("==================================================");
        System.out.println("             🚀 HealthSync에 오신 것을 환영합니다 🚀");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            RecordType recordType = promptRecordType(sc);
            if (recordType == null) {
                break;
            }

            boolean managingType = true;
            while (managingType && running) {
                RecordAction action = promptRecordAction(sc, recordType);
                switch (action) {
                    case ADD -> handleAddRecord(sc, service, recordType);
                    case VIEW -> handleViewRecords(service, recordType);
                    case DELETE -> handleDeleteRecord(sc, service, recordType);
                    case UPDATE -> handleUpdateRecord(sc, service, recordType);
                    case BACK -> managingType = false;
                    case EXIT -> {
                        running = false;
                        managingType = false;
                    }
                }
            }
        }

        System.out.println("👋 이용해 주셔서 감사합니다. 다음에 또 만나요!");
    }

    private static RecordType promptRecordType(Scanner sc) {
        while (true) {
            System.out.println("\n어떤 유형의 기록을 관리할까요?");
            System.out.println("1. 운동 기록");
            System.out.println("2. 수면 기록");
            System.out.println("0. 종료");
            System.out.print("번호를 입력해주세요: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    return RecordType.EXERCISE;
                case "2":
                    return RecordType.SLEEP;
                case "0":
                    return null;
                default:
                    System.out.println("❌ 올바른 번호를 입력해주세요.");
            }
        }
    }

    private static RecordAction promptRecordAction(Scanner sc, RecordType type) {
        while (true) {
            System.out.println("\n" + type.getDisplayName() + " 기록에서 어떤 작업을 진행할까요?");
            System.out.println("1. 기록 추가");
            System.out.println("2. 기록 수정");
            System.out.println("3. 기록 삭제");
            System.out.println("4. 기록 조회");
            System.out.println("0. 이전 메뉴로 돌아가기");
            System.out.println("9. 프로그램 종료");
            System.out.print("번호를 입력해주세요: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    return RecordAction.ADD;
                case "2":
                    return RecordAction.UPDATE;
                case "3":
                    return RecordAction.DELETE;
                case "4":
                    return RecordAction.VIEW;
                case "0":
                    return RecordAction.BACK;
                case "9":
                    return RecordAction.EXIT;
                default:
                    System.out.println("❌ 올바른 번호를 입력해주세요.");
            }
        }
    }

    private static void handleAddRecord(Scanner sc, HealthSyncService service, RecordType type) {
        LocalDate date = promptDate(sc, "기록 날짜(YYYY-MM-DD)를 입력해주세요: ");
        String memo = promptMemo(sc, "메모를 입력해주세요 (없다면 엔터): ");

        switch (type) {
            case EXERCISE -> {
                String exerciseName = promptNonEmpty(sc, "어떤 운동을 하셨나요?: ");
                int exerciseTime = promptPositiveInt(sc, "운동 시간(분)을 입력해주세요: ");
                int calories = promptPositiveInt(sc, "소모 칼로리(kcal)를 입력해주세요: ");
                ExerciseRecord record = new ExerciseRecord(date, memo, exerciseName, exerciseTime, calories);
                service.addRecord(record);
            }
            case SLEEP -> {
                int sleepTime = promptPositiveInt(sc, "수면 시간(시간 단위)을 입력해주세요: ");
                SleepRecord record = new SleepRecord(date, memo, sleepTime);
                service.addRecord(record);
            }
        }
    }

    private static void handleViewRecords(HealthSyncService service, RecordType type) {
        List<? extends TrackableItem> records = getRecordsByType(service, type);
        printRecords(records, type);

        if (!records.isEmpty()) {
            if (type == RecordType.EXERCISE) {
                System.out.println("총 운동 시간: " + service.calculateTotalWorkoutTime() + "분");
            } else {
                System.out.println("총 수면 시간: " + service.calculateTotalSleepTime() + "시간");
            }
        }
    }

    private static void handleDeleteRecord(Scanner sc, HealthSyncService service, RecordType type) {
        List<? extends TrackableItem> records = getRecordsByType(service, type);
        if (records.isEmpty()) {
            System.out.println("삭제할 " + type.getDisplayName() + " 기록이 없습니다.");
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

    private static void handleUpdateRecord(Scanner sc, HealthSyncService service, RecordType type) {
        List<? extends TrackableItem> records = getRecordsByType(service, type);
        if (records.isEmpty()) {
            System.out.println("수정할 " + type.getDisplayName() + " 기록이 없습니다.");
            return;
        }

        System.out.println("\n수정할 기록을 선택해주세요:");
        for (int i = 0; i < records.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, records.get(i).getSummary());
        }

        int index = promptIntInRange(sc, "번호를 입력해주세요: ", 1, records.size());
        TrackableItem item = records.get(index - 1);

        LocalDate newDate = promptDateOrKeep(sc, item.getDate(), "새 날짜를 입력해주세요 (현재: " + item.getDate() + ", 유지하려면 엔터): ");
        String newMemo = promptMemoOrKeep(sc, item.getMemo(), "새 메모를 입력해주세요 (현재 메모 유지: 엔터): ");
        item.setDate(newDate);
        item.setMemo(newMemo);

        if (type == RecordType.EXERCISE) {
            ExerciseRecord record = (ExerciseRecord) item;
            String newName = promptOptionalNonEmpty(sc, record.getExerciseName(), "새 운동명을 입력해주세요 (현재: " + record.getExerciseName() + ", 유지하려면 엔터): ");
            int newTime = promptOptionalPositiveInt(sc, record.getExerciseTime(), "새 운동 시간(분)을 입력해주세요 (현재: " + record.getExerciseTime() + "): ");
            int newCalories = promptOptionalPositiveInt(sc, record.getCalories(), "새 칼로리(kcal)를 입력해주세요 (현재: " + record.getCalories() + "): ");
            record.setExerciseName(newName);
            record.setExerciseTime(newTime);
            record.setCalories(newCalories);
        } else {
            SleepRecord record = (SleepRecord) item;
            int newSleepTime = promptOptionalPositiveInt(sc, record.getSleepTime(), "새 수면 시간(시간)을 입력해주세요 (현재: " + record.getSleepTime() + "): ");
            record.setSleepTime(newSleepTime);
        }

        service.updateRecord(item);
    }

    private static List<? extends TrackableItem> getRecordsByType(HealthSyncService service, RecordType type) {
        return switch (type) {
            case EXERCISE -> service.getExcerciseRecords();
            case SLEEP -> service.getSleepRecords();
        };
    }

    private static void printRecords(List<? extends TrackableItem> records, RecordType type) {
        if (records.isEmpty()) {
            System.out.println("📭 조회할 " + type.getDisplayName() + " 기록이 없습니다.");
            return;
        }

        System.out.println("\n==== " + type.getDisplayName() + " 기록 목록 ====");
        for (TrackableItem item : records) {
            System.out.println(item.getSummary());
        }
        System.out.println("========================");
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

    private static LocalDate promptDateOrKeep(Scanner sc, LocalDate current, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                return current;
            }
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

    private static String promptMemoOrKeep(Scanner sc, String current, String message) {
        System.out.print(message);
        String input = sc.nextLine().trim();
        return input.isEmpty() ? current : input;
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

    private static String promptOptionalNonEmpty(Scanner sc, String current, String message) {
        System.out.print(message);
        String input = sc.nextLine().trim();
        return input.isEmpty() ? current : input;
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

    private static int promptOptionalPositiveInt(Scanner sc, int current, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                return current;
            }
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
