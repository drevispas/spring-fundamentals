package com.example.springfundamentals

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

data class Car(val name: String = "car") {
    init {
        println("Car `$name` is created.")
    }
}

data class CarOwner(val name: String = "carOwner", val car: Car) {
    init {
        println("CarOwner `$name` with `${car.name}` is created.")
    }
}

@Configuration
class BeanConfig {
    @Bean
    @Primary
    fun car1(): Car {
        return Car("car1") // 스프링이 반환객체를 context에 추가
    }

    @Bean
    fun car2(): Car {
        return Car("car2") // 스프링이 반환객체를 context에 추가
    }

    @Bean
    fun long() = 1L

//    @Bean
//    fun hello() = "hello"

    @Bean
    @Primary
    fun carOwner1() = CarOwner("carOwner1", car1()) // car1() @Bean 함수 직접 호출

    @Bean
    fun carOwner2(car: Car) = CarOwner("carOwner2", car) // 인자 carOwner에 해당 하는 bean을 주입시켜준다.
}

@Component // 스프링이 instance를 만들어 context에 추가
data class Bike(val name: String = "bike") {
    init {
        println("Bike `$name` is created.")
    }

    @PostConstruct
    fun post() = println("Right after Bike `$name` is created.")
}

@Configuration
@ComponentScan(basePackages = ["com.example.springfundamentals"]) // springfundamentals 패키지에서 어노테이트된 클래스를 찾아라고 지시
class ComponentConfig // bean 생성 함수 필요없음

@Configuration
class RegisterBeanConfig

@Component
class BikeOwnerInjectedByAutowired {
    val name: String = "bikeOwnerInjectedByAutowired"
    init {
        println("BikeOwnerInjectedByAutowired `$name` is created.")
    }
    @Autowired
    lateinit var bike: Bike
}

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
