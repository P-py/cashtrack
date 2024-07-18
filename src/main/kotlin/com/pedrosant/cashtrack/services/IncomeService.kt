package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.IncomeEntry
import com.pedrosant.cashtrack.dtos.IncomeUpdate
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
    fun register(newIncome:IncomeEntry):IncomeView{
        val new = mapper.mapEntry(newIncome)
        new.id = (incomes.size+1).toLong()
        incomes = incomes.plus(new)
        userService.addIncome(new, new.userId)
        return mapper.mapView(new)
    }
    fun update(updatedIncome:IncomeUpdate):IncomeView{
        val current = incomes.stream().filter { i -> i.id == updatedIncome.id }
            .findFirst()
            .get()
        val update = Income(
            id = current.id,
            incomeLabel = updatedIncome.incomeLabel,
            value = updatedIncome.value,
            type = updatedIncome.type,
            userId = current.userId
        )
        incomes = incomes.minus(current).plus(update)
        userService.updatedIncome(update, current)
        return mapper.mapView(update)
    }
    fun delete(id:Long){
        val deletedIncome = incomes.stream().filter { i -> i.id == id }
            .findFirst()
            .get()
        incomes = incomes.minus(deletedIncome)
        userService.deleteIncome(deletedIncome)
    }
}