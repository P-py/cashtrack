package com.pedrosant.cashtrack.controllers

import com.pedrosant.cashtrack.dtos.UserEntry
import com.pedrosant.cashtrack.dtos.UserUpdate
import com.pedrosant.cashtrack.dtos.UserView
import com.pedrosant.cashtrack.services.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val service:UserService){
    @GetMapping
    fun getList():List<UserView>{
        return service.getUsers()
    }
    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long):UserView{
        return service.getUserById(id)
    }
    @GetMapping("/{userId}/balance")
    fun getBalanceById(@PathVariable userId:Long):Double{
        return service.getBalance(userId)
    }
    @PostMapping
    fun register(@RequestBody @Valid newUser:UserEntry){
        service.registerNewUser(newUser)
    }
    @PutMapping
    fun update(@RequestBody @Valid updatedUser:UserUpdate){
        service.updateUser(updatedUser)
    }
}