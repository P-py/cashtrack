package com.pedrosant.cashtrack.dtos

import com.pedrosant.cashtrack.models.ExpenseType
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class ExpenseEntry(
    @field:NotEmpty(message = "Label must not be empty.")
    @field:NotBlank(message = "Label must not be blank.")
    @field:Size(min = 3, max = 30, message = "Size for label must be between 3 and 30.")
    val expenseLabel:String,
    @field:NotNull(message = "Value must not be empty.")
    @field:Positive(message = "Value must be positive.")
    val value:Double,
    @field:NotNull(message = "Type must not be empty.")
    val type:ExpenseType,
    @field:NotNull(message = "User must be identified.")
    val userId:Long
)