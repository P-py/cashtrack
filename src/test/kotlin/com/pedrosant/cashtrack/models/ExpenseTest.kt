package com.pedrosant.cashtrack.models

object ExpenseTest {
    fun build() = Expense(
        id = 1,
        expenseLabel = "test expense",
        value = 54813.57,
        type = ExpenseType.MONTHLY_ESSENTIAL,
        userCashtrack = UserCashtrackTest.build()
    )
}