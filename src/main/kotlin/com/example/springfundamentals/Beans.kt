package com.example.springfundamentals

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

data class Car(val name: String = "car") {
    init {
        println("Car `$name` is created.")
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
}

@Component // 스프링이 instance를 만들어 context에 추가
data class Bike(val name: String = "bike") {
    init {
        println("Bike `$name` is created.")
    }
}

@Configuration
@ComponentScan(basePackages = ["com.example.springfundamentals"]) // springfundamentals 패키지에서 어노테이트된 클래스를 찾아라고 지시
class ComponentConfig // bean 생성 함수 필요없음
