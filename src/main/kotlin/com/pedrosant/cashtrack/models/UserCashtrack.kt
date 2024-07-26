package com.pedrosant.cashtrack.models

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class UserCashtrack(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null,
    var username:String,
    var email:String,
    val password:String,
    @OneToMany(mappedBy = "userCashtrack", cascade = [CascadeType.ALL], orphanRemoval = true)
    var expenseList:List<Expense> = ArrayList(),
    @OneToMany(mappedBy = "userCashtrack", cascade = [CascadeType.ALL], orphanRemoval = true)
    var incomeList:List<Income> = ArrayList()
)
