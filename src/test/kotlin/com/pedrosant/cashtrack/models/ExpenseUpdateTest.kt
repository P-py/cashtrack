package com.pedrosant.cashtrack.models

import com.pedrosant.cashtrack.dtos.ExpenseUpdate

object ExpenseUpdateTest {
    fun build() = ExpenseUpdate(
        id = 1,
        expenseLabel = "Expense update tester",
        value = 555.5,
        type = ExpenseType.INVESTMENTS
    )
}