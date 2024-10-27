package com.pedrosant.cashtrack.controllers

import com.pedrosant.cashtrack.dtos.ExpenseEntry
import com.pedrosant.cashtrack.dtos.ExpenseUpdate
import com.pedrosant.cashtrack.dtos.ExpenseView
import com.pedrosant.cashtrack.repository.UserRepository
import com.pedrosant.cashtrack.services.ExpenseService
import com.pedrosant.cashtrack.services.TokenService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/expenses")
@SecurityRequirement(name = "bearerAuth")
class ExpenseController(
    private val service:ExpenseService,
    private val tokenService: TokenService,
    private val userRepository: UserRepository
){

    @GetMapping("/admin-list")
    fun getList(
        @PageableDefault(page = 0, size = 10, sort = ["dateCreated"], direction = Sort.Direction.DESC)
        pageable:Pageable
    ):Page<ExpenseView>{
        return service.getExpenses(pageable)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long, @RequestHeader("Authorization") accessToken: String):ExpenseView{
        return service.getExpenseById(id, accessToken)
    }

    @GetMapping
    fun getByUser(
        @RequestHeader("Authorization") accessToken: String,
        @RequestParam(required = false
    ) label:String?):List<ExpenseView> {
        return service.getExpensesByUser(accessToken, label)
    }

    @PostMapping
    @Transactional
    fun register(
            @RequestBody @Valid newExpense:ExpenseEntry,
            uriBuilder:UriComponentsBuilder,
            @RequestHeader("Authorization") accessToken:String
        ):ResponseEntity<ExpenseView>{
        val expenseView = service.register(newExpense, accessToken)
        val uri = uriBuilder.path("/expenses/${expenseView.id}")
            .build()
            .toUri()
        return ResponseEntity.created(uri).body(expenseView)
    }

    @PutMapping
    @Transactional
    fun update(
        @RequestBody @Valid updatedExpense:ExpenseUpdate,
        @RequestHeader("Authorization") accessToken: String
    ):ResponseEntity<ExpenseView>{
        val updateView = service.update(updatedExpense, accessToken)
        return ResponseEntity.ok(updateView)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    fun delete(@PathVariable id:Long, @RequestHeader("Authorization") accessToken: String){
        service.delete(id, accessToken)
    }
}