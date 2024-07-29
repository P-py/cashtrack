package com.pedrosant.cashtrack.auth

data class AuthenticationResponse(
    val accessToken:String,
    val userId:Long?
)
