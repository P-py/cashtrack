package com.pedrosant.cashtrack.models

import com.pedrosant.cashtrack.dtos.IncomeView
import java.time.LocalDateTime

object IncomeViewTest {
    fun build() = IncomeView(
        id = 1,
        incomeLabel = "Income test",
        value = 1095.5,
        type = IncomeType.SALARY,
        dateCreated = LocalDateTime.now(),
        lastUpdatedAt = null
    )
}