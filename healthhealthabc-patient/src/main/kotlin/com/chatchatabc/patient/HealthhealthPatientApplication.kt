package com.chatchatabc.patient

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableDubbo
class HealthhealthPatientApplication

fun main(args: Array<String>) {
    runApplication<HealthhealthPatientApplication>(*args)
}
