package com.pedrosant.cashtrack.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UserUpdate(
    @field:NotEmpty(message = "New username cannot be empty.")
    @field:NotBlank(message = "New username cannot be empty.")
    @field:Size(min = 3, max = 40, message = "Username size must be between 3 and 40 characters.")
    val username:String,
    @field:NotEmpty(message = "New email must not be empty.")
    @field:NotBlank(message = "New email cannot be empty.")
    @field:Size(min = 5, max = 100, message = "Email size must be between 5 and 100 characters.")
    val email:String
)