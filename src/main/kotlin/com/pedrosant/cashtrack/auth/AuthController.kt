package com.pedrosant.cashtrack.auth

import com.pedrosant.cashtrack.services.AuthenticationService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/auth")
class AuthController(
    private val authenticationService:AuthenticationService
){
    @PostMapping
    fun authenticate(@RequestBody authRequest:AuthenticationRequest): AuthenticationResponse {
        val authResponse = authenticationService.authentication(authRequest)
        return authResponse
    }
}