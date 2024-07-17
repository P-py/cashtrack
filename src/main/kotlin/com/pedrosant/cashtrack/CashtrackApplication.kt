package com.pedrosant.cashtrack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CashtrackApplication

fun main(args: Array<String>) {
	runApplication<CashtrackApplication>(*args)
}
