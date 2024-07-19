package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.ExpenseEntry
import com.pedrosant.cashtrack.dtos.ExpenseUpdate
import com.pedrosant.cashtrack.dtos.ExpenseView
import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.mappers.ExpenseMapper
import com.pedrosant.cashtrack.models.Expense
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ExpenseService(
    private var expenses:List<Expense> = ArrayList(),
    private val mapper:ExpenseMapper,
    private val userService:UserService,
    private val notFoundMessage:String = "Oh, something went wrong! Expense not found!"
    ){
    fun getExpenses():List<ExpenseView> {
        return expenses.stream().map {
            e -> mapper.mapView(e)
        }.collect(Collectors.toList())
    }
    fun getExpenseById(id:Long):ExpenseView {
        val result =  expenses.stream().filter { e -> e.id == id }
            .findFirst().orElseThrow{ NotFoundException(notFoundMessage) }
        return mapper.mapView(result)
    }
    fun getExpensesByUser(userId:Long):List<ExpenseView> {
        try {
            userService.getUserById(userId)
        } catch(e:NotFoundException){
            throw(NotFoundException("There is not user for this id!"))
        }
        try {
            return expenses.stream().filter { e -> e.userId == userId }
                .toList().map{
                        e -> mapper.mapView(e)
                }
        } catch (e:NotFoundException) {
            throw(NotFoundException(notFoundMessage))
        }
    }
    fun register(newExpense:ExpenseEntry):ExpenseView{
        val newEntry = mapper.mapEntry(newExpense)
        newEntry.id = (expenses.size+1).toLong()
        expenses = expenses.plus(newEntry)
        userService.addExpense(newEntry, newEntry.userId)
        return mapper.mapView(newEntry)
    }
    fun update(updatedExpense:ExpenseUpdate):ExpenseView{
        val current = expenses.stream().filter{ e -> e.id == updatedExpense.id }
            .findFirst()
            .orElseThrow{ NotFoundException(notFoundMessage) }
        val update = Expense(
            id = current.id,
            expenseLabel = updatedExpense.expenseLabel,
            value = updatedExpense.value,
            type = updatedExpense.type,
            userId = current.userId
        )
        expenses = expenses.minus(current).plus(update)
        userService.updateExpense(update, current)
        return mapper.mapView(update)
    }
    fun delete(id:Long) {
        val deletedExpense = expenses.stream().filter { e -> e.id == id }
            .findFirst()
            .orElseThrow{ NotFoundException(notFoundMessage) }
        expenses = expenses.minus(deletedExpense)
        userService.deleteExpense(deletedExpense)
    }
}