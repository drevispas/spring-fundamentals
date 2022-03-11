package com.example.springfundamentals

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.function.Supplier

fun main() {
    configurationClass()
//    componentAnnotation()
//    registerBean()
//    composeBean()
//    composeBeanWithAutowired()
//    composeBeanWithQualified()
}

fun composeBeanWithQualified() {
    val context = AnnotationConfigApplicationContext(ComponentConfig::class.java)
    val carOwnerWithQualified = context.getBean(CarOwnerWithQualified::class.java)
    println("${carOwnerWithQualified.name} owns ${carOwnerWithQualified.car.name}")
}

fun composeBeanWithAutowired() {
    val context = AnnotationConfigApplicationContext(ComponentConfig::class.java)
    val bikeOwner = context.getBean(BikeOwner::class.java)
    println("${bikeOwner.name} owns ${bikeOwner.bike.name}")
}

fun composeBean() {
    val context = AnnotationConfigApplicationContext(BeanConfig::class.java)
    val carOwner1 = context.getBean("owner1", CarOwner::class.java)
    println("${carOwner1.name} owns ${carOwner1.car.name}")
    val carOwner2 = context.getBean("owner2", CarOwner::class.java)
    println("${carOwner2.name} owns ${carOwner2.car.name}")
}

private fun configurationClass() {
    val context = AnnotationConfigApplicationContext(BeanConfig::class.java)
    val car2 = context.getBean("car2", Car::class.java)
    println(car2.name)
    val long = context.getBean(Long::class.java)
    println(long)
    val hello = context.getBean(String::class.java)
    println(hello)
    val car1 = context.getBean(Car::class.java)
    println(car1.name)
}

private fun componentAnnotation() {
    val context = AnnotationConfigApplicationContext(ComponentConfig::class.java)
    val bike = context.getBean(Bike::class.java)
    println(bike)
}

private fun registerBean() {
    val context = AnnotationConfigApplicationContext(RegisterBeanConfig::class.java)
    val supplier: Supplier<Car> = Supplier { Car("car3") }
    context.registerBean("car3", Car::class.java, supplier)
    val car = context.getBean(Car::class.java)
    println(car.name)
}
