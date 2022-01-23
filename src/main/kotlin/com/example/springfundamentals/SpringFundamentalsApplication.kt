package com.example.springfundamentals

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringFundamentalsApplication

fun main(args: Array<String>) {
    runApplication<SpringFundamentalsApplication>(*args)
}
