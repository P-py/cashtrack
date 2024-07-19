package com.pedrosant.cashtrack.dtos

import com.pedrosant.cashtrack.models.IncomeType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class IncomeUpdate(
    @field:NotNull(message = "You must specify an income to be updated.")
    val id:Long,
    @field:NotEmpty(message = "Update label must no be empty.")
    @field:NotBlank(message = "Update label must not be empty.")
    @field:Size(min = 3, max = 30, message = "Label size must be between 3 and 30.")
    val incomeLabel:String,
    @field:NotNull(message = "Value must no be empty.")
    @field:Positive(message = "Value must be positive.")
    val value:Double,
    @field:NotNull(message = "Expense type must be specified.")
    val type:IncomeType,
)
