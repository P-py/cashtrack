package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.auth.AuthenticationRequest
import com.pedrosant.cashtrack.auth.AuthenticationResponse
import com.pedrosant.cashtrack.config.JwtProperties
import com.pedrosant.cashtrack.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.Date

@Service
class AuthenticationService(
    private val authManager:AuthenticationManager,
    private val userDetailsService:CustomUserDetailsService,
    private val tokenService:TokenService,
    private val jwtProperties:JwtProperties,
    private val usersRepository:UserRepository
){
    fun authentication(authRequest:AuthenticationRequest):AuthenticationResponse{
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )
        val user = userDetailsService.loadUserByUsername(authRequest.email)
        val accessToken = tokenService.generate(
            userDetails = user,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
        )
        return AuthenticationResponse(
            accessToken = accessToken,
            userId = usersRepository.findByEmail(user.username)?.id
        )
    }
}