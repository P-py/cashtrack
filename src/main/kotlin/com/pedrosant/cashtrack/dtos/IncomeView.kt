package com.pedrosant.cashtrack.dtos

import com.pedrosant.cashtrack.models.IncomeType
import java.time.LocalDateTime

data class IncomeView(
    val id:Long?,
    val incomeLabel:String,
    val value:Double,
    val type:IncomeType,
    val dateCreated:LocalDateTime,
    val lastUpdatedAt:LocalDateTime?
)