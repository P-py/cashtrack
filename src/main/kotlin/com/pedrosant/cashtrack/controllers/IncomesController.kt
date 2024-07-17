package com.pedrosant.cashtrack.controllers

import com.pedrosant.cashtrack.dtos.IncomeEntry
import com.pedrosant.cashtrack.dtos.IncomeView
import com.pedrosant.cashtrack.models.Income
import com.pedrosant.cashtrack.services.IncomeService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/incomes")
class IncomesController(private val service:IncomeService){
    @GetMapping
    fun getList():List<IncomeView>{
        return service.getIncomes()
    }
    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long):IncomeView{
        return service.getIncomeById(id)
    }
    @GetMapping("/byuser/{userId}")
    fun getByUser(@PathVariable userId:Long):List<IncomeView>{
        return service.getIncomesByUser(userId)
    }
    @PostMapping
    fun register(@RequestBody @Valid newIncome:IncomeEntry){
        service.register(newIncome)
    }
}