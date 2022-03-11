package com.example.springfundamentals

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class Car(val name: String = "car") {
    init {
        println("Car `$name` is created.")
    }
}

@Configuration
class BeanConfig {
    @Bean
    fun car1(): Car {
        return Car("car1") // 스프링이 반환객체를 context에 추가
    }

    @Bean
    fun car2(): Car {
        return Car("car2") // 스프링이 반환객체를 context에 추가
    }

    @Bean
    fun long() = 1L

    @Bean
    fun hello() = "hello"
}
