package com.pedrosant.cashtrack.auth

data class AuthenticationRequest(
    val email:String,
    val password:String
)