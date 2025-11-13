# Java HealthSync 프로젝트 상세 요구사항 명세

이 명세는 Java의 핵심 및 고급 문법(OOP, 컬렉션, I/O, 스트림, 람다, 예외)을 총체적으로 복습할 수 있도록 설계되었습니다.

## 1. 데이터 모델링 및 OOP 요구사항

### 1.1. 추상 클래스 (`TrackableItem.java` 기반)

- **목표 문법:** 추상화 (`abstract class`), 캡슐화, 상속, 불변성 (`final`)
- **요구 사항:**
    1. 모든 기록 항목이 공통으로 가지는 속성 (`ID`, `날짜`, `메모`)을 포함해야 합니다.
    2. 고유 식별자 (`ID`)는 `final`로 선언하고, 생성자에서 `UUID.randomUUID()`를 사용하여 자동 생성해야 합니다.
    3. `Serializable` 인터페이스를 구현하여 파일 입출력(직렬화)을 지원해야 합니다.
    4. 모든 하위 클래스가 반드시 구현해야 하는 추상 메서드 (`getSummary()`)를 포함해야 합니다.

### 1.2. 구체 클래스 (하위 클래스)

- **목표 문법:** 상속, 다형성, 메서드 오버라이딩
- **요구 사항:**
    1. `SleepRecord` 클래스: 수면 시간(정수) 속성을 추가하고 `getSummary()`를 오버라이드해야 합니다.
    2. `ExerciseRecord` 클래스: 운동 종류(문자열), 시간(정수), 소모 칼로리(정수) 속성을 추가하고 `getSummary()`를 오버라이드해야 합니다.

## 2. 핵심 로직 및 컬렉션 요구사항

### 2.1. 서비스 클래스 (`TrackerService` 역할)

- **목표 문법:** 컬렉션 (`List`/`Map`), 제네릭스 (`List<T>`), 캡슐화
- **요구 사항:**
    1. 모든 `TrackableItem` 객체를 저장하고 관리하기 위한 컬렉션 (`List<TrackableItem>` 권장)을 내부 필드로 선언해야 합니다. **(컬렉션 적용)**
    2. 외부에서 이 컬렉션에 직접 접근하여 수정하는 것을 방지하기 위해, 컬렉션을 반환할 때 불변 리스트 (`List.copyOf()`)를 사용하거나 복사본을 반환하여 캡슐화를 유지해야 합니다.
    3. **CRUD (생성, 조회, 수정, 삭제)** 기능을 구현해야 합니다.

### 2.2. 제네릭 메서드 (고급)

- **목표 문법:** 사용자 정의 제네릭 메서드, 와일드카드, 타입 캐스팅
- **요구 사항:**
    1. 특정 하위 클래스 타입 (`SleepRecord` 또는 `ExerciseRecord`)의 기록만 필터링하여 반환하는 제네릭 메서드를 구현해야 합니다.
        - 예: `public <T extends TrackableItem> List<T> getRecordsByType(Class<T> type)`

## 3. 고급 Java 문법 (Java 8+) 요구사항

### 3.1. 스트림 API 및 람다 표현식

- **목표 문법:** 스트림의 파이프라인 구성 (`filter`, `map`, `collect`), 메서드 참조, 람다
- **요구 사항:**
    1. **보고서 기능:** 모든 기록 중에서 특정 조건(예: 금주의 기록)을 만족하는 데이터를 필터링하기 위해 **스트림의 `filter()`*와 **람다 표현식** 또는 **메서드 참조**를 사용해야 합니다.
    2. **통계 계산:** 수면 시간 평균 (`mapToInt().average().orElse()`) 또는 칼로리 총합 (`summingInt()`)을 계산할 때 스트림의 최종 연산자를 사용해야 합니다.
    3. **데이터 정렬:** 기록 목록을 날짜 순으로 정렬할 때 `List.stream().sorted(Comparator.comparing(Lambda))`와 같은 람다 기반 정렬을 사용해야 합니다.

### 3.2. Optional 클래스

등장 배경 → 값이 없으면 NPE(Null Pointer Exception) 발생으로 처리하기 어려워짐

값이 있거나 없음을 감싸는 Wrapper 클래스임 

null 대신 Optional 객체 반환

🔹 사용 전/후  예시 코드  🔹

```java
TrackableItem item = service.getRecordByDate(date);
if(item != null) {
    System.out.println(item.getSummary());
} else {
    System.out.println("없음");
}

Optional<TrackableItem> recordOpt = service.getRecordByDate(date);

recordOpt.ifPresentOrElse(
    item -> System.out.println(item.getSummary()),  // 값 있으면 실행
    () -> System.out.println("해당 날짜 기록이 없습니다.") // 값 없으면 실행
);

```

Optional 객체 안에 값이 있으면 `.get()` 또는 람다로 바로 사용 가능!! 

값이 없으면 null 체크 없이도 안전하게 처리 가능

- 
- **목표 문법:** Null 안정성, `orElse`, `ifPresentOrElse`
- **요구 사항:**
    1. ID를 기반으로 특정 기록을 조회하는 메서드는 `null` 대신 **`Optional<TrackableItem>`*을 반환해야 합니다.
    2. 기록을 삭제하거나 수정할 때, `Optional`의 `map()`, `ifPresent()`, 또는 `ifPresentOrElse()`와 같은 메서드를 활용하여 기록이 존재하지 않는 경우의 처리를 간결하게 구현해야 합니다.

## 4. 안정성 및 예외 처리 요구사항

### 4.1. 예외 처리

- **목표 문법:** `try-catch`, 다중 `catch`, `throws`
- **요구 사항:**
    1. 사용자 입력 시 숫자가 아닌 값이 들어왔을 때 (`InputMismatchException`)나 날짜 형식이 잘못되었을 때 (`DateTimeParseException`)를 메인 클래스에서 반드시 `try-catch`로 처리해야 합니다.
    2. 날짜나 시간이 유효 범위를 벗어났을 경우를 위해 **사용자 정의 예외 (`InvalidInputException`)**를 생성하고, 이를 `throws` 키워드를 사용하여 메서드에 전파해야 합니다.

### 4.2. 파일 입출력 (File I/O)

- **목표 문법:** 직렬화, `try-with-resources`, `FileInputStream/ObjectInputStream`
- **요구 사항:**
    1. 프로그램 시작 시 저장된 파일로부터 데이터를 **역직렬화하여 로드**해야 합니다.
    2. 프로그램 종료 시 현재 기록 데이터를 파일에 **직렬화하여 저장**해야 합니다.
    3. 파일 I/O 작업 시에는 리소스 자동 반환을 위해 **`try-with-resources` 구문**을 사용해야 합니다.

[OOP / Abstract class](https://www.notion.so/OOP-Abstract-class-29269cd3230a80d5adcec65f04c649cf?pvs=21)

[UUID.randomUUID() / Serializable 인터페이스](https://www.notion.so/UUID-randomUUID-Serializable-29269cd3230a803485dfc43210a9dea9?pvs=21)

### 🦋 제네릭 타입 기본 구조

```java
List<타입> list = new ArrayList<>();
```

`List<타입>` : 참조 변수 타입

`new ArrayList<>()` : 실제 객체 생성 

`<>`  : 안에는 담을 수 있는 요소 타입을 지정

예시 

```java
List<String> list = new ArrayList<>();
```

- list에 String 타입만 담을 수 있음
- ArrayList 안에도 String 만 담을 수 있음

### 추상 클래스 타입으로도 가능!

```java
List<TrackableITem> item = new ArrayList<>();
```

TrackableItem은 추상 클래스인데, 참조 타입으로도는, 가능

추상 클래스는 객체 자체는 만들 수 없지만, 타입 제한으로 쓰는 것 가능함!

### <> 안에는 왜 아무것도 안 넣음?🙊

Java 7 +

컴파일러가 왼쪽 참조 변수 추론해서 ArrayList 도 TrackableItem 타입 갖게 됨.

### 🧩[상속 규칙] 모든 생성자는 부모 생성자를 먼저 호출해야 함

그래서 만약 자식 생성자 첫 문장에 super(,) 가 없다면, 컴파일러가 자동으로 오류를 발생시킬 확률이 높음.

이유는? super() 을 내부적으로 호출시키는데, 만약 super() 부모 생성자에 메서드가 없는 생성자가 없으면 오류 발생시킴

그래서 부모 생성자에 맞는 파라미터를 넣어 줘야 함!

### 향상된 for 문

`for(TrackableItem item : records)` 

records에 있는 요소들을 하나씩 꺼내서 item 변수에 담고 반복한다는 의미

### for 문으로 쓰면 이런 느낌이겠지?

```java
for(int i =0 ;i<records.size();i++){
		Trackable item = records.get(i);
		}
```

### Stream 의 역할

> 배열이나 리스트를 데이터의 흐름(stream)으로 바꿔주는 것!
> 

그럼으로써 filter, forEach, map, sorted 등과 같이 연산자 메서드를 이용할 수 있다.

### ForEach 메서드

- 반환값 없음
- 데이터 변경에 사용하면 안 됨

### ❌반환값 없음

```java
List<Integer> evens = numbers.stream()
														 .filter(n -> n%2)
														 .forEach(System.out::println)
```

이렇게 하면 오류야!!

왜냐면 forEach는 반환값만 단순히 “출력”만 하고 끝나니 evens에 저장할 값이 없다!

> 즉, forEach()는 “반환값 없는 끝맺음 메서드”야
> 

### 데이터 변경에 사용하면 안 됨

```java
List<Integer> evens = new ArrayList<>(List.of(1,2,3,4));

list.forEach(n->list.remove(n)); //ConcurrentMoidificationException 발생
						
```

`forEach` 안에서 리스트를 수정하면 동시에 **순회+삭제**가 일어나서 에러남

따라서 forEach는 “읽기 전용 순회용”으로만 써야 함

### forEach VS map

**`map`** : 각 요소들에 대한 반환값 있음. 새로운 스트림 생성함 보통, 데이터의 형태/타입 변환(transform)할 때 이용

**`forEach`** : 각 요소들에 대한 부수 효과 적용. 반환값 없음 보통, 출력/로그 상태 변경 등에 사용

예시 코드

```java
List<Integer> nums = List.of(1,2,3,4);

// map: 각 요소를 2배로 바꿔 새로운 리스트로 얻음
Dobule nums = nums.stream()
									.map(n -> n*2)
									.toList();
									
// forEach: 요소를 하나씩 출력(반환 없음)
nums.forEach(n -> System.out.println(n));
```

### 생성자 내 super() 메서드가 자동으로 실행되는 이유

자식 클래스의 객체가 생성될 때, 부모 클래스의 멤버(필드/메서드)도 함께 메모리에 올라와 초기화 되어야 하기 때문

### 생성자 VS 메서드

- 생성자 : 객체가 메모리에 생성될 때 딱 한 번 호출되어 필드를 초기화하는 특수 메서드(반환 타입 없음)
- 메서드 : 객체의 기능을 수행하거나, 객체의 상태(필드 값)를 읽거나 변경하는 일반적인 함수

### parse

문자열 → 날짜(LocalDate), 숫자(int, double 등) 같은 데이터 타입으로 바꿀 때 자주 쓰임

### 기본 개념 정리

```java
LocalDate date = LocalDate.parse("2025-10-01");
System.out.println(date); 
```

- `parse()`는 문자열이 “yyyy-MM-dd” 형식일 때만 제대로 작동함
- 만약 “2025/10/01” 처럼 다른 구분자를 쓰면 `DateTimeParseException` 이 발생함

### 다른 형식의 입력 처리하기

사용자가 **“2025/10/23”**처럼 다른 형식으로 입력할 수도 있으니, `DateTimeFormatte`r를 이용해서 원하는 형식을 지정할 수 있다.

```java
DateTiemFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
LocalDate date = LocalDate.parse("2025/10/23", formatter);
```

### 🚨잘못된 입력 처리 + 반복 입력 예제🚨

사용자가 잘못된 형식으로 입력했을 때.

오류를 잡고 다시 입력받는 구조는 try-catch + 반복문으로 쉽게 만들 수 있다.

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DateInputExample {
		public static void main(String[] args){
				Scanner sc = new Scanner(System.in);
				LocalDate date = null;
				DateFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
				while(true) {
						System.out.println("날짜를 입력하세요 (형식 : yyyy-MM-dd) ");
						String input = sc.nextLine();
						
						try {
							date = LocalDate.parse(input, formatter);
							break;
							} catch (DateTimeParseException e){
									System.out.println("형식이 올바르지 않습니다. 다시 입력하세요💢");
									}
							}
							System.out.println("입력된 날짜 : " + date);
							sc.close();
							}
				}
```

## [ + 추가 ] 사용자의 입력 받기

우선, 서비스 계층은 TrackableItem을 받아 리스트에 추가/조회하는 책임만 수행하고 있으므로, 메인 로직에서 어떤 유형의 레코드를 만들지 결정해 addRecord로 넘기래

### 1. 사용자의 흐름부터 상상해보기

- 사용자가 앱을 켜면 어떤 선택지를 볼까?
- 추가 → 조회 → 삭제 → 종료
