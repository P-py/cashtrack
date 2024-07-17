package com.pedrosant.cashtrack.models

import java.time.LocalDateTime

data class Expense(
    var id:Long? = null,
    val expenseLabel:String,
    val value:Double,
    val type:ExpenseType,
    val dateCreated:LocalDateTime = LocalDateTime.now(),
    val userId:Long
)
