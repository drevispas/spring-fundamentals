package com.example.springfundamentals

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.function.Supplier

fun main() {
//    configurationClass()
//    componentAnnotation()
    registerBean()
//    composeBean()
//    composeBeanWithAutowired()
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
    context.registerBean(
        /* beanName = */ "car3",
        /* beanClass = */ Car::class.java,
        /* supplier = */ supplier)
    val car = context.getBean(Car::class.java)
    println(car)
}
