package com.pedrosant.cashtrack.models

import java.time.LocalDateTime

data class Income(
    var id:Long? = null,
    val incomeLabel:String,
    val value:Double,
    val type:IncomeType,
    val dateCreated:LocalDateTime = LocalDateTime.now(),
    val userId:Long
)