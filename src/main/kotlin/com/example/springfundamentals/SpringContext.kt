package com.example.springfundamentals

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.function.Supplier

fun main() {
//    configurationClass()
//    componentAnnotation()
//    registerBean()
//    composeBean()
    composeBeanWithAutowired()
//    composeBeanWithQualified()
}

private fun configurationClass() {
    val context = AnnotationConfigApplicationContext(BeanConfig::class.java) // context 생성할 때 넘겨줄 수 있음

    val car1 = context.getBean(Car::class.java)
    println(car1.name)

    val car2 = context.getBean("car2", Car::class.java)
    println(car2.name)

    val long = context.getBean(Long::class.java)
    println(long)

    val hello = context.getBean(String::class.java)
    println(hello)
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
    println(car)
}

fun composeBean() {
    val context = AnnotationConfigApplicationContext(BeanConfig::class.java)

    val carOwner1 = context.getBean(CarOwner::class.java)
    println("${carOwner1.name} owns ${carOwner1.car.name}.")

    val carOwner1Again = context.getBean(CarOwner::class.java)
    println("${carOwner1.name} owns ${carOwner1.car.name}.")

    val carOwner2 = context.getBean("carOwner2", CarOwner::class.java)
    println("${carOwner2.name} owns ${carOwner2.car.name}.")
}

fun composeBeanWithAutowired() {
    val context = AnnotationConfigApplicationContext(ComponentConfig::class.java)

    val bikeOwnerInjectedByAutowired = context.getBean(BikeOwnerInjectedByAutowired::class.java)
    println("${bikeOwnerInjectedByAutowired.name} owns a ${bikeOwnerInjectedByAutowired.bike.name}.")

    val bikeOwnerInjectedByConstructor = context.getBean(BikeOwnerInjectedByConstructor::class.java)
    println("${bikeOwnerInjectedByConstructor.name} owns a ${bikeOwnerInjectedByConstructor.bike.name}.")
}
