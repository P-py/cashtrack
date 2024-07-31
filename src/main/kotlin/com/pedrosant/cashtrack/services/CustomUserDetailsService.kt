package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

typealias ApplicationUser = com.pedrosant.cashtrack.models.UserCashtrack

@Service
class CustomUserDetailsService(
    private val usersRepository:UserRepository,
):UserDetailsService{
    override fun loadUserByUsername(username:String):UserDetails =
        usersRepository.findByEmail(username)?.mapToUserDetails()
            ?: throw RuntimeException("User not found!")

    private fun ApplicationUser.mapToUserDetails():UserDetails =
        User.builder()
            .username(this.email)
            .password(this.password)
            .roles(this.role.name)
            .build()
}