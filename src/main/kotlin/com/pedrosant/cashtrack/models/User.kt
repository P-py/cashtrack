package com.pedrosant.cashtrack.models

data class User(
    var id:Long? = null,
    val username:String,
    val email:String,
    var expenseList:List<Expense> = ArrayList(),
    var incomeList:List<Income> = ArrayList()
)
