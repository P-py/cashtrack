package com.pedrosant.cashtrack.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null,
    val username:String,
    val email:String,
    @OneToMany(mappedBy = "userId")
    var expenseList:List<Expense> = ArrayList(),
    @OneToMany(mappedBy = "userId")
    var incomeList:List<Income> = ArrayList()
)
