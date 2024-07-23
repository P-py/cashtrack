package com.pedrosant.cashtrack.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Income(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null,
    var incomeLabel:String,
    var value:Double,
    @Enumerated(value = EnumType.STRING)
    var type:IncomeType,
    val dateCreated:LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    val userCashtrack:UserCashtrack
)