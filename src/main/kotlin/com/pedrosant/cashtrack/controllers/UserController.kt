package com.pedrosant.cashtrack.controllers

import com.pedrosant.cashtrack.dtos.BalanceView
import com.pedrosant.cashtrack.dtos.UserEntry
import com.pedrosant.cashtrack.dtos.UserUpdate
import com.pedrosant.cashtrack.dtos.UserView
import com.pedrosant.cashtrack.services.UserService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@CrossOrigin
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
class UserController(private val service:UserService) {

    @GetMapping
    fun getList(
        @PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC)
        pageable:Pageable
    ):Page<UserView>{
        return service.getUsers(pageable)
    }

    @GetMapping("/account")
    fun getById(@RequestHeader(value = "Authorization") accessToken:String):UserView{
        return service.getUserById(accessToken, null)
    }

    @GetMapping("/balance")
    fun getBalanceById(@RequestHeader(value = "Authorization") accessToken:String):BalanceView{
        return service.getBalance(accessToken)
    }

    @PostMapping
    @Transactional
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
    @Transactional
    fun update(
        @RequestBody @Valid updatedUser:UserUpdate,
        @RequestHeader("Authorization") accessToken: String
    ):ResponseEntity<UserView>{
        val updateView = service.updateUser(updatedUser, accessToken)
        return ResponseEntity.ok(updateView)
    }

    @DeleteMapping("/delete-my-account")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@RequestHeader("Authorization") accessToken: String){
        service.delete(accessToken)
    }
}