package com.pedrosant.cashtrack.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Income(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null,
    val incomeLabel:String,
    val value:Double,
    @ManyToOne
    @Enumerated(value = EnumType.STRING)
    val type:IncomeType,
    val dateCreated:LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    @JoinColumn(name = "userId")
    val userId:Long
)