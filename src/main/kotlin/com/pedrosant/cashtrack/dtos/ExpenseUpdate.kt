package com.pedrosant.cashtrack.dtos

import com.pedrosant.cashtrack.models.ExpenseType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class ExpenseUpdate(
    @field:NotNull(message = "You must specify an expense to be updated.")
    val id:Long,
    @field:NotEmpty(message = "Update label must no be empty.")
    @field:NotBlank(message = "Update label must no be empty.")
    @field:Size(min = 3, max = 30, message = "Label size must be between 3 and 30.")
    val expenseLabel:String,
    @field:NotNull(message = "Value must no be empty.")
    @field:Positive(message = "Value must be positive.")
    val value:Double,
    @field:NotNull(message = "Expense type must be specified.")
    val type:ExpenseType
)
