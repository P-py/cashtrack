package com.pedrosant.cashtrack.controllers

import com.pedrosant.cashtrack.dtos.IncomeEntry
import com.pedrosant.cashtrack.dtos.IncomeUpdate
import com.pedrosant.cashtrack.dtos.IncomeView
import com.pedrosant.cashtrack.services.IncomeService
import jakarta.transaction.Transactional
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
    @Transactional
    fun register(
            @RequestBody @Valid newIncome:IncomeEntry,
            uriBuilder:UriComponentsBuilder
        ):ResponseEntity<IncomeView>{
        val createdIncome = service.register(newIncome)
        val uri = uriBuilder.path("/incomes/${createdIncome.id}")
            .build()
            .toUri()
        return ResponseEntity.created(uri).body(createdIncome)
    }
    @PutMapping
    @Transactional
    fun updated(@RequestBody @Valid updatedIncome:IncomeUpdate):ResponseEntity<IncomeView>{
        val updateView = service.update(updatedIncome)
        return ResponseEntity.ok(updateView)
    }
    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id:Long){
        service.delete(id)
    }
}