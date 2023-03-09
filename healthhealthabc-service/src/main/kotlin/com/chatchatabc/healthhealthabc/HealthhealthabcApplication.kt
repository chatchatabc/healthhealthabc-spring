package com.chatchatabc.healthhealthabc

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableDubbo
class HealthhealthabcApplication

fun main(args: Array<String>) {
    runApplication<HealthhealthabcApplication>(*args)
}
