package com.pedrosant.cashtrack.repository

import com.pedrosant.cashtrack.models.UserCashtrack
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository:JpaRepository<UserCashtrack, Long> {
    // Query is auto implemented by JPA Repository
    fun findByEmail(email:String):UserCashtrack
}