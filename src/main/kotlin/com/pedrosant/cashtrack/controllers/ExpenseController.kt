package com.pedrosant.cashtrack.controllers

import com.pedrosant.cashtrack.dtos.ExpenseEntry
import com.pedrosant.cashtrack.dtos.ExpenseUpdate
import com.pedrosant.cashtrack.dtos.ExpenseView
import com.pedrosant.cashtrack.services.ExpenseService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/expenses")
class ExpenseController(private val service: ExpenseService){
    @GetMapping
    fun getList():List<ExpenseView>{
        return service.getExpenses()
    }
    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long):ExpenseView{
        return service.getExpenseById(id)
    }
    @GetMapping("/byuser/{userId}")
    fun getByUser(@PathVariable userId:Long):List<ExpenseView>{
        return service.getExpensesByUser(userId)
    }
    @PostMapping
    fun register(@RequestBody @Valid newExpense:ExpenseEntry){
        service.register(newExpense)
    }
    @PutMapping
    fun update(@RequestBody @Valid updatedExpense:ExpenseUpdate){
        service.update(updatedExpense)
    }
}