package com.pedrosant.cashtrack.controllers

import com.pedrosant.cashtrack.dtos.ExpenseEntry
import com.pedrosant.cashtrack.dtos.ExpenseUpdate
import com.pedrosant.cashtrack.dtos.ExpenseView
import com.pedrosant.cashtrack.services.ExpenseService
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
class ExpenseController(private val service:ExpenseService){
    @GetMapping
    fun getList(
        @PageableDefault(page = 0, size = 10, sort = ["dateCreated"], direction = Sort.Direction.DESC)
        pageable:Pageable
    ):Page<ExpenseView>{
        return service.getExpenses(pageable)
    }
    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long):ExpenseView{
        return service.getExpenseById(id)
    }
    @GetMapping("/byuser/{userId}")
    fun getByUser(@PathVariable userId:Long, @RequestParam(required = false) label:String?):List<ExpenseView>{
        return service.getExpensesByUser(userId, label)
    }
    @PostMapping
    @Transactional
    fun register(
            @RequestBody @Valid newExpense:ExpenseEntry,
            uriBuilder:UriComponentsBuilder
        ):ResponseEntity<ExpenseView>{
        val expenseView = service.register(newExpense)
        val uri = uriBuilder.path("/expenses/${expenseView.id}")
            .build()
            .toUri()
        return ResponseEntity.created(uri).body(expenseView)
    }
    @PutMapping
    @Transactional
    fun update(@RequestBody @Valid updatedExpense:ExpenseUpdate):ResponseEntity<ExpenseView>{
        val updateView = service.update(updatedExpense)
        return ResponseEntity.ok(updateView)
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    fun delete(@PathVariable id:Long){
        service.delete(id)
    }
}