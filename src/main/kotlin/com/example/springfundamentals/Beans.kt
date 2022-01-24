package com.example.springfundamentals

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.*
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

class Car(val name: String = "car") {
    init {
        println("Car $name is created")
    }
}

@Configuration
class BeanConfig {
    @Bean
    fun car2(): Car {
        return Car("car2")
    }
    @Bean
    fun long() = 1L
//    @Bean
//    fun hello() = "hello!"
    @Bean
    @Primary
    fun car1() = Car("car1")
    @Bean
    fun carOwner1() = CarOwner("carOwner1", car2())
    @Bean
    fun carOwner2(car: Car) = CarOwner("carOwner2", car)
}

@Component
data class Bike(val name: String = "bike") {
    init {
        println("something pre")
    }

    @PostConstruct
    fun post() = println("something post")
}

@Configuration
@ComponentScan(basePackages = ["com.example.springfundamentals"])
class ComponentConfig

@Configuration
class RegisterBeanConfig

class CarOwner(val name: String = "carOwner", val car: Car)

@Component
class BikeOwner {
    val name: String = "bikeOwner"
//    @Autowired
//    lateinit var bike: Bike
    val bike: Bike
    constructor(bike: Bike){
        this.bike = bike
    }
}

@Component
class CarOwnerWithQualified(val name: String = "carOwnerWithQualified", @Qualifier("car2") val car: Car)
