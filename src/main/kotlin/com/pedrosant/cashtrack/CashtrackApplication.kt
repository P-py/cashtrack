	package com.pedrosant.cashtrack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class CashtrackApplication

fun main(args: Array<String>) {
	runApplication<CashtrackApplication>(*args)
}
