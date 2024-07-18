package com.pedrosant.cashtrack.dtos

import com.pedrosant.cashtrack.models.IncomeType
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class IncomeUpdate(
    @field:NotNull
    val id:Long,
    @field:NotEmpty
    @field:Size(min = 3, max = 30)
    val incomeLabel:String,
    @field:NotNull
    val value:Double,
    @field:NotEmpty
    val type:IncomeType,
)
