package com.pedrosant.cashtrack.models

import com.pedrosant.cashtrack.dtos.IncomeEntry

object IncomeEntryTest {
    fun build() = IncomeEntry(
        incomeLabel = "Income tester",
        value = 95.0,
        type = IncomeType.EXTRA,
        userId = 1
    )
}