package com.pedrosant.cashtrack.dtos

data class BalanceView(
    val totalIncomes:Double,
    val totalExpenses:Double,
    val balance:Double
)