package com.pedrosant.cashtrack.auth

import com.pedrosant.cashtrack.services.AuthenticationService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationService:AuthenticationService
){
    @PostMapping
    fun authenticate(@RequestBody authRequest:AuthenticationRequest, response:HttpServletResponse):AuthenticationResponse{
        val authResponse = authenticationService.authentication(authRequest)
        response.addCookie(Cookie("userId", authResponse.userId.toString()))
        return authResponse
    }
}