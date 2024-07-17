package com.pedrosant.cashtrack.dtos

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserUpdate(
    @field:NotNull
    val userId:Long,
    @field:NotEmpty
    @field:Size(min = 3, max = 40)
    val username:String,
    @field:NotEmpty
    @field:Size(min = 5, max = 100)
    val email:String
)