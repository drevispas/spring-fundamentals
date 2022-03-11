# spring-fundamentals
(1교시 시작)
# **1. Prerequisites**

## 1.1. Preparation
- 컴퓨터 준비
    - Intellij Idea
    - JDK 11: https://www.azul.com/downloads/?package=jdk

## 1.2. 프로젝트 생성하기
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

## 1.3. 용어
- Framework
  - 여러 프로젝트를 진행하다 보니 core biz logic을 제외한 다른 부분들을 반복적으로 구현하고 있다는 사실을 알겠되었습니다.
  - 문제가 생겼을 때 같은 프레임웍을 쓰는 사람들로부터 도움을 쉽게 받을 수 있습니다.
  - 하지만 틀에 갇혀 있어야 하며 그 틀을 배워야 합니다.
- Spring
  - 여러 하위 프레임웍 프로젝트를 가진 복잡한 시스템입니다.
    - Spring Core: context, aspect
    - Spring Data: persistence, ORM
    - Spring MVC
    - Spring Cloud
    - Spring Boot: convention over configuration
    - ...
  - 이런 것들이 다 필요한가 -> 필요한 부분만 취하면 됩니다.
- DI (Dependency Injection): 필요한 객체를 넘겨줍니다.
- IoC (Inversion of Control): 애플리케이션이 아니라 프레임웍이 조종합니다.
- 일명 스프링 트라이앵글:
![Alt text](src/main/resources/static/spring-triangle.png?raw=true "Spring Triangle")


# **2. Application context**
- 스프링이 관리할 모든 instance들을 모아 놓은 메모리 영역
- 스프링은 context에 없는 객체들은 관리하지 못합니다.
- context에 저장되는 instance를 bean이라고 부릅니다.

## 2.1. Spring context에 bean 추가하기

### 2.1.1. Bean 추가하는 방법들
- @Bean
- @Component, @Repository, @Service, @Controller
- .registerBean()

### 2.1.2. context 의존성 추가하기
- spring-context: context를 사용할 수 있게 해주는 외부 라이브러리
    - build.gradle.kts 열기
    - Intellij 하위 메뉴에서 Dependencies 열기
    - 검색창에 spring-context 입력
    - org.springframework:spring-context를 추가
    - Load Gradle Changes 버튼 클릭
    - 외부 라이브러리를 빌드 설정에 추가하면 다운로드 받습니다.

### 2.1.3. 데모용 class 작성
- Base package `com.example.springfundamentals`에 kotlin class `SpringContext` 추가
    ```kotlin
    fun main() {
        configurationClass()
        //componentAnnotation()
        //registerBean()
        //composeBean()
        //composeBeanWithAutowired()
        //composeBeanWithQualified()
    }
    ```
- Base package `com.example.springfundamentals`에 kotlin class `Beans` 추가
    ```kotlin 
    class Car(val name: String = "car")
    ```

### 2.1.4. @Bean으로 bean 추가하기
- @Configuration: 일반 class를 설정 class로 만듭니다.
- @Bean 함수: context 초기화 때 실행시켜서 context에 추가시킵니다.
    - 함수이름이 bean 이름이 되므로 보통 명사로 짓는다. 보통 bean class 이름과 동일하게 합니다.
- `Beans`를 아래와 같이 수정
    ```kotlin
    class car(val name: String = "car") {
      init {
        println("Car `$name` is created.")
      }
    }

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
        val car1 = context.getBean(Car::class.java) // 
        println(car1.name)
        val long = context.getBean(Long::class.java)
        println(long)
    }
    ```
### 2.1.5. 연습 문제
- [ ] "hello"를 반환하는 bean을 context에 추가하고 main()에서 테스트하기
  - @Bean fun hello() = "hello"
- [ ] 타입 대신 bean 이름으로 검색해서 얻기
  - context.getBean("car2", Car::class.java)
- [ ] 같은 타입의 bean 추가하기
  - @Bean fun car1("car1") = "car1"
- [ ] 같은 타입이 복수일 때 기본값
  - @Bean @Primary

(1교시 끝)

### 2.1.6. @Component로 bean 추가하기
- @Component: class에 표시하면 스프링이 생성해서 context에 추가해줍니다.
- @Configuration @ComponentScan: 스프링한테 표시한 class들이 어디 있는지 알려줍니다.
- `Beans`에 아래 추가
    ```kotlin
    @Component // 스프링이 instance를 만들어 context에 추가
    class Bike(val name: String = "bike") {
        init {
            println("Bike `$name` is created.")
        }
    }

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
        - 원인: BaseConfig.hello() bean이 primary constructor에 주입되었습니다.
        - 조치: @Bean fun hello()를 주석으로 막습니다.
- boilerplate 코드가 적은 @Component 방식을 더욱 선호합니다.

### 2.1.7. @PostConstruct
- 스프링이 bean 생성 직후 실행할 것 정의
- build.gradle.ktx: `javax.annotation:javax.annotation-api` 의존 추가
- `Beans.Bike`를 수정
    ```kotlin
    data class Bike(val name: String = "bike") {
        init {
            println("Bike `$name` is created.")
        }

        @PostConstruct
        fun post() = println("Right after Bike `$name` is created.")
    }
    ```

### 2.1.8. .registerBean()으로 bean 추가하기
Spring context에 선언이 아닌 프로그램으로 bean을 추가하는 방법이다. 프로그램적으로 특정 조건에 맞는 인스턴스만 bean으로 만들 수 있습니다.
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
        context.registerBean(
            /* beanName = */ "car3",
            /* beanClass = */ Car::class.java,
            /* supplier = */ supplier)
        val car = context.getBean(Car::class.java)
        println(car.name)
    }
    ```
- registerBean()의 3번째 파라미터는 Java에서 말하는 functional interface 구현체로 bean 객체를 공급하는 역할입니다. Supplier { } 안에는 인스턴스를 반환하는 람다식을 넣습니다. 

## 2.2. Bean 주입시키기
- 객체들 간의 의존관계가 필요할 경우 context에 있는 bean을 가져와 사용할 수 있습니다.
- UML 기준으로 dependency, association, aggregation, composition 관계를 맺을 때 사용할 수 있습니다.
  ![Alt text](src/main/resources/static/uml-relationship.png?raw=true "Spring Triangle")
- 방법들
    - @Configuration 이용해서 주입시키하기
    - 함수 인자에 @Bean을 주입시키기
    - 객체 속성에 @Autowired 설정해서 주입시키기

### 2.2.1. @Bean 함수 내부에서 다른 @Bean 함수 직접 호출해서 주입시키기
- `Beans`에 수정 및 추가
  ```kotlin
    data class CarOwner(val name: String = "carOwner", val car: Car) {
        init {
            println("CarOwner `$name` with `${car.name}` is created.")
        }
    }
  
    @Configuration
    class BeanConfig {
        // ...
        @Bean
        fun carOwner1() = CarOwner("carOwner1", car1()) // car1() @Bean 함수 직접 호출
    }
  ```
  - @Configuration에 CarOnwer와 Car bean을 등록합니다. 이 때 car2() bean 함수를 직접 호출해서 car 멤버 속성에 주입하였습니다.
  - CarOwner와 Car 객체 간에 aggregation (has-a) 관계가 만들어집니다.
  - **만일 context 안에 이미 car2 bean이 존재한다면, car2() 함수가 실제로는 호출되지 않고 context에서 bean을 가져와서 주입시켜 줍니다.**
- `SpringContext`에 추가
  ```kotlin
  fun composeBean() {
    val context = AnnotationConfigApplicationContext(BeanConfig::class.java)
    val carOwner1 = context.getBean(CarOwner::class.java)
    println("${carOwner1.name} owns ${carOwner1.car.name}.")
  }
  ```
  - 정상적으로 주입되었는지 확인하는 코드입니다.

### 2.2.2. 연습 문제
-`SpringContext` 수정
  ```kotlin
  fun composeBean() {
    val context = AnnotationConfigApplicationContext(BeanConfig::class.java)
  
    val carOwner1 = context.getBean(CarOwner::class.java)
    println("${carOwner1.name} owns ${carOwner1.car.name}.")
  
    val carOwner1Again = context.getBean(CarOwner::class.java)
    println("${carOwner1.name} owns ${carOwner1.car.name}.")
  }
  ```
- [ ] 문제: 두번째 getBean() 호출하면 CarOwner("carOwner1", car1())에 의하여 @Bean car1()이 또 다시 호출될까요? 

### 2.2.3. @Bean 함수 인자에 다른 bean을 주입시키기
- 스프링이 context에 있는 bean을 꺼내서 인자값으로 넘겨줍니다 (inject).
- `Beans`에 수정
  ```kotlin
  class BeanConfig {
    // ...
    @Bean
    @Primary
    fun carOwner1() = CarOwner("carOwner1", car1()) // car1() @Bean 함수 직접 호출
    
    @Bean
    fun carOwner2(car: Car) = CarOwner("carOwner2", car) // 인자 carOwner에 해당 하는 bean을 주입시켜줍니다.
  }
  ```
  - context에 CarOwner 타입의 bean이 있으면 carOwner 값으로 주입시켜줍니다. 따라서 context bean이 @Bean으로 등록되었는지 @Component로 주입되었는지 경로는 상관없습니다.
  - Spring이라는 IoC container가 DI(Dependency Injection)을 수행하는 장면입니다.
- `SpringContext`에 추가
  ```kotlin
  fun composeBean() {
      // ...
      val carOwner2 = context.getBean("carOwner2", CarOwner::class.java)
      println("${carOwner2.name} owns ${carOwner2.car.name}.")
  }
  ```

### 2.2.4. 객체 속성에 @Autowired 설정해서 주입시키기
- 스프링이 @Autwored로 표시된 클래스 속성에 bean을 주입시키게 합니다.
- 3가지 변형이 있습니다.
    - 필드에 직접 명시해서 주입
    - constructor 인자를 통해서 주입
    - setter를 통해서 주입

- 클래스 필드에 @Autowired 직접 명시해서 주입
    - 테스트코드에 주로 쓰이는 방식
    - @Bean보다는 주로 @Component와 결합해서 쓰입니다.
    - `Beans`에 추가
      ```kotlin
      @Component
      class BikeOwnerInjectedByAutowired {
        val name: String = "bikeOwnerInjectedByAutowired"
        init {
            println("BikeOwnerInjectedByAutowired `$name` is created.")
        }
    
        @Autowired
        lateinit var bike: Bike
      }
      ```
- `SpringContext`에 추가
  ```kotlin
  fun composeBeanWithAutowired() {
    val context = AnnotationConfigApplicationContext(ComponentConfig::class.java)
    val bikeOwnerInjectedByAutowired = context.getBean(BikeOwnerInjectedByAutowired::class.java)
    println("${bikeOwnerInjectedByAutowired.name} owns a ${bikeOwnerInjectedByAutowired.bike.name}.")
  }
  ```
  - 단점: `val`을 사용할 수 없습니다. `lateint var` 대신 `val`로 바꾸면 컴파일 오류가 납니다.
- constructor 인자를 통해서 주입
  - 실제 코드에서 가장 일반적인 방식
  - 장점: 주입되는 속성을 `val`로 선언할 수 있습니다.
  - `Beans`에 수정
    ```kotlin
    @Component
    class BikeOwnerInjectedByConstructor {
        val name: String = "bikeOwnerInjectedByConstructor"
        init {
            println("BikeOwnerInjectedByConstructor `$name` is created.")
        }
        val bike: Bike
        //@Autowired // constructor에는 생략 가능
        constructor(bike: Bike){
            this.bike = bike
        }
    }
    ```
  - `SpringContext`
    ```kotlin
    fun composeBeanWithAutowired() {
        val context = AnnotationConfigApplicationContext(ComponentConfig::class.java)
  
        val bikeOwnerInjectedByAutowired = context.getBean(BikeOwnerInjectedByAutowired::class.java)
        println("${bikeOwnerInjectedByAutowired.name} owns a ${bikeOwnerInjectedByAutowired.bike.name}.")
  
        val bikeOwnerInjectedByConstructor = context.getBean(BikeOwnerInjectedByConstructor::class.java)
        println("${bikeOwnerInjectedByConstructor.name} owns a ${bikeOwnerInjectedByConstructor.bike.name}.")
    }
    ```
  - setter를 통해서 주입
    - 현재는 거의 안 쓰입니다.
    - 코틀린에서는 이 방식을 흉내내기가 어려워 데모는 스킵합니다. (자바는 setter 함수 위에 @Autowired만 추가하면 됨)

### 2.2.5. Bean 연결시 고려사항
- 만날 수 있는 오류들
    - Bean이 또 다른 bean을 필요로 하는데 context에 없는 경우
    - 참조하는 다른 bean 연결고리를 따라가다가 자기 자신을 만나는 경우
    - 동일한 타입의 bean들이 여러 개 발견되는 경우
        - @Component 클래스에 @Primary 부여하기
        - constructor 인자에 @Qualifier 부여하기
    - @Qualifier 로 원하는 bean 주입시키기
      - `Beans`에 추가
        ```kotlin
        @Component
        class CarOwnerSelectedByQualifier(
          val name: String = "carOwnerSelectedByQualifier",
          @Qualifier("car2")
          val car: Car
        ) {
          init {
              println("CarOwnerSelectedByQualifier `$name` is created.")
          }
        }
        ```
      - `SpringContext`에 추가
        ```kotlin
        fun composeBeanWithQualified() {
            val context = AnnotationConfigApplicationContext(ComponentConfig::class.java)
  
            val carOwnerSelectedByQualifier = context.getBean(CarOwnerSelectedByQualifier::class.java)
            println("${carOwnerSelectedByQualifier.name} owns a ${carOwnerSelectedByQualifier.car.name}.")
        }
        ```
      
### 2.2.6. 연습 문제
- [ ] 위에서 `@Qualifier`를 제거하면 출력 결과는 어떨까요?

## 2.3. Bean과 Abstraction
클래스는 이렇게 사용해라고 외부에 공개하여 약속한 클라이언트와의 계약이 있고, 그 계약을 정의해 놓은 것을 인터페이스라고 합시다. 계약과 구현물을 분리하기 위해서 추상화를 사용하게 됩니다. 클래스가 구현체 대신 인터페이스를 사용하면, 다른 구현체로 바뀌더라도 클라이언트 코드를 수정할 필요가 없는 장점이 있습니다.

### 2.3.1. 객체의 설계
- 명명 규칙: 이름을 잘 지어야 합니다. 유즈케이스를 처리하는 객체는 ~Service, DB 연결을 처리하는 객체는 ~Repository 등으로 이름 짓는 게 보통입니다.
- 책임의 분리: 기본적으로 객체는 하나의 책임만을 갖도록 설계합니다. 책임이 둘일 경우 객체를 분리하여야 합니다. **Single Reponsibility Principle**
- 변경에 저항: 일부 기능 변경 때문에 나까지 바뀌지 않게 설계합니다. 사용 기능을 인터페이스화 합니다. **Open-Closed Principle**
- 상속에 주의: 부모 객체의 계약 사항을 행간까지 읽어야 합니다. 상속을 기피하는 것도 방법입니다. **Liscov Substitution Principle**
- 계약의 유지: 계약에서 제공하는 기능이 적도록 설계합니다. 제공 기능이 많은 경우 인터페이스를 분리합니다. **Interface Segregation Principle**
- 느슨한 결합: 추상에만 의존하도록 설계합니다. 구현체 말고 인터페이스로 변수를 선언합니다. **Dependency Inversion Principle**

### 2.3.2. 순진하게 구현하기
(그림은 https://alexnault.dev/dependency-inversion-principle-in-functional-typescript 에서 가져 옴)
![Alt text](src/main/resources/static/traditional-dependency.svg?raw=true "Spring Triangle")


### 2.3.3. 능숙하게 구현하기
![Alt text](src/main/resources/static/dependency-inversion.svg?raw=true "Spring Triangle")

## 다음에 할 것들:
- [ ] Bean scope
- [ ] AOP
- [ ] Spring MVC
