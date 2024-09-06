package com.pedrosant.cashtrack.controllers

import com.pedrosant.cashtrack.dtos.IncomeEntry
import com.pedrosant.cashtrack.dtos.IncomeUpdate
import com.pedrosant.cashtrack.dtos.IncomeView
import com.pedrosant.cashtrack.services.IncomeService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/incomes")
@SecurityRequirement(name = "bearerAuth")
class IncomesController(private val service:IncomeService){
    @GetMapping("/admin-list")
    fun getList(
        @PageableDefault(page = 0, size = 10, sort = ["dateCreated"], direction = Sort.Direction.DESC)
        pageable:Pageable
    ):Page<IncomeView>{
        return service.getIncomes(pageable)
    }
    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long, @CookieValue(value = "userId") userId:Long):IncomeView{
        return service.getIncomeById(id, userId)
    }
    @GetMapping
    fun getByUser(@CookieValue(value = "userId") userId:Long,
                  @RequestParam(required = false) label:String?
    ):List<IncomeView>{
        return service.getIncomesByUser(userId, label)
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
    fun updated(
        @RequestBody @Valid updatedIncome:IncomeUpdate,
        @CookieValue("userId") userId:Long
    ):ResponseEntity<IncomeView>{
        val updateView = service.update(updatedIncome, userId)
        return ResponseEntity.ok(updateView)
    }
    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id:Long, @CookieValue("userId") userId:Long){
        service.delete(id, userId)
    }
}