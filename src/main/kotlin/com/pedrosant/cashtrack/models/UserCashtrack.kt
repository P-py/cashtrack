package com.pedrosant.cashtrack.models

import jakarta.persistence.*

@Entity
data class UserCashtrack(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null,
    var username:String,
    var email:String,
    val password:String,
    @Enumerated(value = EnumType.STRING)
    val role:Role,
    @OneToMany(mappedBy = "userCashtrack", cascade = [CascadeType.ALL], orphanRemoval = true)
    var expenseList:List<Expense> = ArrayList(),
    @OneToMany(mappedBy = "userCashtrack", cascade = [CascadeType.ALL], orphanRemoval = true)
    var incomeList:List<Income> = ArrayList()
)

enum class Role {
    USER,
    ADMIN
}