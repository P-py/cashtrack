package com.pedrosant.cashtrack.dtos

import com.pedrosant.cashtrack.models.IncomeType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Positive
import javax.validation.constraints.NotEmpty

data class IncomeEntry(
    @field:NotEmpty(message = "Label must not be empty.")
    @field:NotBlank(message = "Label must not be blank.")
    @field:Size(min = 3, max = 30, message = "Label size must be between 3 and 30.")
    val incomeLabel:String,
    @field:NotNull(message = "Value must not be empty.")
    @field:Positive
    val value:Double,
    @field:NotNull(message = "Type must not be empty.")
    val type:IncomeType,
    @field:NotNull(message = "User must be identified.")
    val userId:Long
)
