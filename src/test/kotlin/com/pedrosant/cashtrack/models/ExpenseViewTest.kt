package com.pedrosant.cashtrack.models

import com.pedrosant.cashtrack.dtos.ExpenseView
import java.time.LocalDateTime

object ExpenseViewTest {
    fun build() = ExpenseView(
        id = 1,
        expenseLabel = "Expense tester",
        value = 27.0,
        type = ExpenseType.MONTHLY_ESSENTIAL,
        dateCreated = LocalDateTime.now(),
        lastUpdatedAt = null
    )
}