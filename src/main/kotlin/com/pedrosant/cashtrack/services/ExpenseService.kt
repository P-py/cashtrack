package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.ExpenseEntry
import com.pedrosant.cashtrack.dtos.ExpenseUpdate
import com.pedrosant.cashtrack.dtos.ExpenseView
import com.pedrosant.cashtrack.mappers.ExpenseMapper
import com.pedrosant.cashtrack.models.Expense
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ExpenseService(
    private var expenses:List<Expense> = ArrayList(),
    private val mapper:ExpenseMapper,
    private val userService:UserService
    ){
    fun getExpenses():List<ExpenseView> {
        return expenses.stream().map {
            e -> mapper.mapView(e)
        }.collect(Collectors.toList())
    }
    fun getExpenseById(id:Long):ExpenseView {
        val result =  expenses.stream().filter { e -> e.id == id }
            .findFirst().get()
        return mapper.mapView(result)
    }
    fun getExpensesByUser(userId:Long):List<ExpenseView> {
        return expenses.stream().filter { e -> e.userId == userId }
            .toList().map{
                e -> mapper.mapView(e)
            }
    }
    fun register(newExpense:ExpenseEntry) {
        val newEntry = mapper.mapEntry(newExpense)
        newEntry.id = (expenses.size+1).toLong()
        expenses = expenses.plus(mapper.mapEntry(newExpense))
        userService.addExpense(newEntry, newEntry.userId)
    }
    fun update(updatedExpense:ExpenseUpdate){
        val current = expenses.stream().filter{ e -> e.id == updatedExpense.id }
            .findFirst().get()
        val update = Expense(
            id = current.id,
            expenseLabel = updatedExpense.expenseLabel,
            value = updatedExpense.value,
            type = updatedExpense.type,
            userId = current.userId
        )
    }
}