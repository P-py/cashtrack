package com.pedrosant.cashtrack.controllers

import com.pedrosant.cashtrack.dtos.UserEntry
import com.pedrosant.cashtrack.dtos.UserUpdate
import com.pedrosant.cashtrack.dtos.UserView
import com.pedrosant.cashtrack.mappers.UserMapper
import com.pedrosant.cashtrack.services.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/users")
class UserController(
    private val service:UserService,
    private val mapper:UserMapper
    ){
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
    fun register(
            @RequestBody @Valid newUser:UserEntry,
            uriBuilder:UriComponentsBuilder
        ):ResponseEntity<UserView>{
        val userView = service.registerNewUser(newUser)
        val uri = uriBuilder.path("/users/${userView.id}")
            .build()
            .toUri()
        return ResponseEntity.created(uri).body(userView)
    }
    @PutMapping
    fun update(@RequestBody @Valid updatedUser:UserUpdate):ResponseEntity<UserView>{
        val updateView = service.updateUser(updatedUser)
        return ResponseEntity.ok(updateView)
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id:Long){
        service.delete(id)
    }
}