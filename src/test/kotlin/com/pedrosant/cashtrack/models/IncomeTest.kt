package com.pedrosant.cashtrack.models

object IncomeTest {
    fun build() = Income(
        id = 1,
        incomeLabel = "test income",
        value = 54813.57,
        type = IncomeType.SALARY,
        userCashtrack = UserCashtrackTest.build()
    )
}