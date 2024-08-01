package com.pedrosant.cashtrack.dtos

import com.pedrosant.cashtrack.models.ExpenseType
import java.time.LocalDateTime

data class ExpenseView(
    val id:Long?,
    val expenseLabel:String,
    val value:Double,
    val type:ExpenseType,
    val dateCreated:LocalDateTime,
    val lastUpdatedAt:LocalDateTime?
)
