package com.pedrosant.cashtrack.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class UserEntry(
    @field:NotEmpty(message = "Email must not be empty.")
    @field:NotBlank(message = "Email must not be blank.")
    @field:Size(min = 5, max = 100, message = "Email size must be between 5 and 100 characters.")
    val email:String,
    @field:NotEmpty(message = "Username must not be empty.")
    @field:NotBlank(message = "Username must not be blank.")
    @field:Size(min = 3, max = 40, message = "Username size must be between 3 and 40.")
    val username:String,
    @field:NotEmpty(message = "Password must no be empty.")
    @field:NotBlank(message = "Password must no be blank.")
    val password:String
)
