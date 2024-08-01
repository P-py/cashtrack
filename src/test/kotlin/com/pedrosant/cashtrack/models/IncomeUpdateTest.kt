package com.pedrosant.cashtrack.models

import com.pedrosant.cashtrack.dtos.IncomeUpdate

object IncomeUpdateTest {
    fun build() = IncomeUpdate(
        id = 1,
        incomeLabel = "Income update tester",
        value = 10.0,
        type = IncomeType.GIFT
    )
}