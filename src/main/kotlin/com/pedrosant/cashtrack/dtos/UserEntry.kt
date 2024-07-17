package com.pedrosant.cashtrack.dtos

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class UserEntry(
    @field:NotEmpty @field:Size(min = 5, max = 100) val email:String,
    @field:NotEmpty @field:Size(min = 3, max = 40) val username:String
)
