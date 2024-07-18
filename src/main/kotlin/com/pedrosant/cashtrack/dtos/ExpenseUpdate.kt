package com.pedrosant.cashtrack.dtos

import com.pedrosant.cashtrack.models.ExpenseType
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size
import javax.validation.constraints.NotNull

data class ExpenseUpdate(
    @field:NotNull
    val id:Long,
    @field:NotEmpty
    @field:Size(min = 3, max = 30)
    val expenseLabel:String,
    @field:NotNull
    val value:Double,
    @field:NotEmpty
    val type:ExpenseType
)
