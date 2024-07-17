package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.IncomeEntry
import com.pedrosant.cashtrack.dtos.IncomeView
import com.pedrosant.cashtrack.mappers.IncomeMapper
import com.pedrosant.cashtrack.models.Income
import org.springframework.stereotype.Service

@Service
class IncomeService(
    private var incomes:List<Income>,
    private val mapper:IncomeMapper,
    private val userService:UserService
    ){
    fun getIncomes():List<IncomeView>{
        return incomes.stream().map { i -> mapper.mapView(i) }.toList()
    }
    fun getIncomeById(id:Long):IncomeView{
        return mapper.mapView(incomes.stream().filter { i -> i.id == id }
            .findFirst().get())
    }
    fun getIncomesByUser(userId:Long):List<IncomeView>{
        return incomes.stream()
            .filter{ i -> i.userId == userId }
            .toList().map {
                i -> mapper.mapView(i)
            }
    }
    fun register(newIncome:IncomeEntry){
        val new = mapper.mapEntry(newIncome)
        new.id = (incomes.size+1).toLong()
        incomes = incomes.plus(new)
        userService.addIncome(new, new.userId)
    }
}