# spring-fundamentals
(1교시 시작)
# **Prerequisites**

## Preparation
- 컴퓨터 준비
    - Intellij Idea
    - JDK 11: https://www.azul.com/downloads/?package=jdk

## 프로젝트 생성하기
- File > New > Project... > Spring Initializr
    - Name: demo
    - Language: Kotlin
    - Type: Gradle
    - Group: com.example
    - Artificat: springfundamentals
    - Java: 11
    - Packaging: Jar
    - Dependencies: 아무 것도 선택하지 않고 Finish
- 프로젝트 수정
    - .gitignore 추가
    - build.gradle.kts: implementation("org.springframework.boot:spring-boot-starter") 라인을 주석 처리
    - @SpringBootApplication: annotation 포함 해당 클래스 전체를 주석 처리
- IDE project structure
    - src/main/java: 소스코드가 저장되는 곳
    - src/main/resources: 설정 및 정적파일이 저장되는 곳
    - src/test/java: 테스트코드가 저장되는 곳
    - gradle.build, pom.xml: 빌드 설정 파일
    - External Libraries: JDK와 외부 의존 라이브러리

## 용어
- DI (Dependency Injection): 필요한 객체를 넘겨준다.
- IoC (Inversion of Control): 애플리케이션이 아니라 프레임웍이 조종한다.

# **Application context**
- 스프링이 관리할 모든 instance들을 모아 놓은 메모리 영역
- 스프링은 context에 없는 객체들은 관리하지 못함
- context에 저장되는 instance를 bean이라고 부름

## Spring context에 bean 추가하기

### Bean 추가하는 방법들
- @Bean
- @Component, @Repository, @Service, @Controller
- .registerBean()

### context 의존성 추가하기
- spring-context: context를 사용할 수 있게 해주는 외부 라이브러리
    - build.gradle.kts 열기
    - Intellij 하위 메뉴에서 Dependencies 열기
    - 검색창에 spring-context 입력
    - org.springframework:spring-context를 추가
    - Load Gradle Changes 버튼 클릭
    - 외부 라이브러리를 빌드 설정에 추가하면 다운로드 받음

### 데모용 class 작성
- Base package `com.example.springfundamentals`에 kotlin class `SpringContext` 추가
    ```kotlin
    fun main() {
        configurationClass()
        //componentAnnotation()
        //registerBean()
        //composeBean()
    }
    ```
- Base package `com.example.springfundamentals`에 kotlin class `Beans` 추가
    ```kotlin 
    class Car(val name: String = "car")
    ```

### @Bean으로 bean 추가하기
- @Configuration: 일반 class를 설정 class로 만듦
- @Bean 함수: context 초기화 때 실행시켜서 context에 추가시킴
    - 함수이름이 bean 이름이 되므로 보통 명사로 짓는다. 보통 bean class 이름과 동일하게 한다.
- `Beans`를 아래와 같이 수정
    ```kotlin
    class car(val name: String = "car")

    @Configuration
    class BeanConfig {
        @Bean
        fun car2() {
            return Car("car2") // 스프링이 반환객체를 context에 추가
        }

        @Bean
        fun long() = 1L
    }
    ```
- `SpringContext`에 다음 추가 후 main()을 실행
    ```kotlin
    private fun configurationClass() {
        val context = AnnotationConfigApplicationContext(BeanConfig::class.java) // context 생성할 때 넘겨줄 수 있음
        val car = context.getBean(Car::class.java) // 
        println(car.name)
        val long = context.getBean(Long::class.java)
        println(long)
    }
    ```
- 연습 문제
    - [ ] "hello"를 반환하는 bean을 context에 추가하고 main()에서 테스트하기
        - @Bean fun hello() = "hello"
    - [ ] 타입 대신 bean 이름으로 검색해서 얻기
        - context.getBean("car2", Car::class.java)
    - [ ] 같은 타입의 bean 추가하기
        - @Bean fun car1("car1") = "car1"
    - [ ] 같은 타입이 복수일 때 기본값
        - @Bean @Primary

### @Component로 bean 추가하기
- @Component: class에 표시하면 스프링이 생성해서 context에 추가해 줌
- @Configuration @ComponentScan: 스프링한테 표시한 class들이 어디 있는지 알려 줌
- `Beans`에 아래 추가
    ```kotlin
    @Component // 스프링이 instance를 만들어 context에 추가
    class Bike(val name: String = "bike")

    @Configuration
    @ComponentScan(basePackages = ["com.example.springfundamentals"]) // springfundamentals 패키지에서 어노테이트된 클래스를 찾아라고 지시
    class ComponentConfig // bean 생성 함수 필요없음
    ```
- `SpringContext`에 다음 추가
    ```kotlin
    private fun componentAnnotation() {
        val context = AnnotationConfigApplicationContext(ComponentConfig::class.java)
        val bike = context.getBean(Bike::class.java)
        println(bike)
    }
    ```
    - bike.name == "hello"인 문제 발생
        - 원인: BaseConfig.hello() bean이 primary constructor에 주입됨
        - 조치: @Bean fun hello()를 주석으로 막을 것
- boilerplate 코드가 적은 @Component 방식을 더욱 선호

### @PostConstruct
- 스프링이 bean 생성 직후 실행할 것 정의
- build.gradle.ktx: `javax.annotation:javax.annotation-api` 의존 추가
- `Beans.Bike`를 수정
    ```kotlin
    data class Bike(val name: String = "bike") {
        init {
            println("something pre")
        }

        @PostConstruct
        fun post() = println("something post")
    }
    ```

### .registerBean()으로 bean 추가하기
- `Beans`에 추가
    ```kotlin
    @Configuration
    class RegisterBeanConfig
    ```
- `SpringContext`에 추가
    ```kotlin
    private fun registerBean() {
        val context = AnnotationConfigApplicationContext(RegisterBeanConfig::class.java)
        val supplier: Supplier<Car> = Supplier { Car("car3") }
        context.registerBean("car3", Car::class.java, supplier)
        val car = context.getBean(Car::class.java)
        println(car.name)
    }
    ```

(1교시 끝)

(2교시 시작)

## Bean 끼리 결합하기
- Spring context에 있는 어떤 bean이 또 다른 bean을 사용할 수 있다.
- 방법들
    - @Bean 함수 호출해서 결합하기
    - 함수 인자에 @Bean을 주입시키기
    - 객체 속성에 @Autowired 설정해서 주입시키기

### @Bean 함수 호출해서 결합하기
- `Beans`에 수정 및 추가
  ```kotlin
    @Configuration
    class BeanConfig {
        // ...
        @Bean
        fun carOwner1() = Owner("carOwner1", car2())
    }

    class CarOwner(val name: String = "carOwner", val car: Car)
  ```
- `SpringContext`에 추가
  ```kotlin
  fun composeBean() {
    val context = AnnotationConfigApplicationContext(BeanConfig::class.java)
    val carOwner1 = context.getBean(CarOwner::class.java)
    println("${carOwner1.name} owns ${carOwner1.car.name}")
  }
  ```
- [ ] 문제: carOwner1()의 car2() 호출은 또 하나의 bean을 생성하는 것일까?

### 함수 인자에 @Bean을 주입시키기
- 스프링이 context에 있는 bean을 꺼내서 인자값으로 넘겨준다 (inject).
- `Beans`에 수정
  ```kotlin
  class BeanConfig {
    // ...
    @Bean
    fun carOwner2(carOwner: CarOwner) = CarOwner("carOwner2", car)
  }
  ```
- `SpringContext`에 추가
  ```kotlin
  ```

### 객체 속성에 @Autowired 설정해서 주입시키기
- 스프링이 @Autwored로 표시된 클래스 속성에 bean을 주입시키게 한다.
- 3가지 변형이 있다.
    - 필드에 직접 명시해서 주입
    - constructor 인자를 통해서 주입
    - setter를 통해서 주입

- 클래스 필드에 @Autowired 직접 명시해서 주입
    - 테스트코드에 주로 쓰이는 방식
    - @Bean보다는 주로 @Component와 결합해서 쓰인다.
    - `Beans`에 추가
      ```kotlin
      @Component
      class BikeOwner {
          val name: String = "bikeOwner"
          @Autowired
          lateinit var bike: Bike
      }
      ```
    - `SpringContext`에 추가
      ```kotlin
      fun composeBeanWithAutowired() {
          val context = AnnotationConfigApplicationContext(ComponentConfig::class.java)
          val bikeOwner = context.getBean(BikeOwner::class.java)
          println("${bikeOwner.name} owns ${bikeOwner.bike.name}")
      }
      ```
    - 단점: `val`을 사용할 수 없다. `lateint var` 대신 `val`로 바꾸면 컴파일 오류난다.
- constructor 인자를 통해서 주입
    - 실제 코드에서 가장 일반적인 방식
    - 장점: 주입되는 속성을 `val`로 선언할 수 있다.
    - `Beans`에 수정
      ```kotlin
      @Component
      class BikeOwner {
          val name: String = "bikeOwner"
          val bike: Bike
          //@Autowired // constructor에는 생략 가능
          constructor(bike: Bike){
              this.bike = bike
          }
      }
      ```
    - `SpringContext`은 수정하지 않음
- setter를 통해서 주입
    - 현재는 거의 안 쓰인다.
    - 코틀린에서는 이 방식을 흉내내기가 어려워 데모는 스킵 (자바는 setter 함수 위에 @Autowired만 추가하면 됨)

### Bean 연결시 고려사항
- 만날 수 있는 오류들
    - Bean이 또 다른 bean을 필요로 하는데 context에 없는 경우
    - 참조하는 다른 bean 연결고리를 따라가다가 자기 자신을 만나는 경우
    - 동일한 타입의 bean들이 여러 개 발견되는 경우
        - @Component 클래스에 @Primary 부여하기
        - constructor 인자에 @Qualifier 부여하기
- @Qaulified로 원하는 bean 주입시키기
    - `Beans`에 추가
      ```kotlin
      @Component
      class CarOwnerWithQualified(val name: String = "carOwnerWithQualified", @Qualifier("car2") val car: Car)
      ```
    - `SpringContext`에 추가
      ```kotlin
      fun composeBeanWithQualified() {
          val context = AnnotationConfigApplicationContext(ComponentConfig::class.java)
          val carOwnerWithQualified = context.getBean(CarOwnerWithQualified::class.java)
          println("${carOwnerWithQualified.name} owns ${carOwnerWithQualified.car.name}")
      }
      ```
    - 문제
        - [ ] 위에서 `@Qualified`를 제거하면 출력 결과는 어떨까?
