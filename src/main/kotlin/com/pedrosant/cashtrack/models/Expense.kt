package com.pedrosant.cashtrack.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Expense(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null,
    var expenseLabel:String,
    var value:Double,
    @Enumerated(value = EnumType.STRING)
    var type:ExpenseType,
    val dateCreated:LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    val userCashtrack:UserCashtrack,
    var lastUpdatedAt:LocalDateTime? = null
)