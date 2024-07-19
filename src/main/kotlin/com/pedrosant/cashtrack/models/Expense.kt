package com.pedrosant.cashtrack.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Expense(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null,
    val expenseLabel:String,
    val value:Double,
    @ManyToOne
    @Enumerated(value = EnumType.STRING)
    val type:ExpenseType,
    val dateCreated:LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    @JoinColumn(name = "userId")
    val userId:Long
)