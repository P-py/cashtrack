package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.ExpenseEntry
import com.pedrosant.cashtrack.dtos.ExpenseUpdate
import com.pedrosant.cashtrack.dtos.ExpenseView
import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.mappers.ExpenseMapper
import com.pedrosant.cashtrack.repository.ExpenseRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ExpenseService(
    private var expensesRepository:ExpenseRepository,
    private val mapper:ExpenseMapper,
    private val userService:UserService,
    private val notFoundMessage:String = "Oh, something went wrong! Expense not found!"
    ){
    fun getExpenses(pageable:Pageable):Page<ExpenseView> {
//        return expenses.stream().map {
//            e -> mapper.mapView(e)
//        }.collect(Collectors.toList())
        return expensesRepository.findAll(pageable)
            .map { e -> mapper.mapView(e) }
    }
    fun getExpenseById(id:Long):ExpenseView {
//        val result =  expenses.stream().filter { e -> e.id == id }
//            .findFirst().orElseThrow{ NotFoundException(notFoundMessage) }
        return mapper.mapView(expensesRepository.getReferenceById(id))
    }
    fun getExpensesByUser(userId:Long, label:String?):List<ExpenseView> {
//        try {
        runCatching {
            userService.getUserById(userId)
        }.onFailure {
            throw(NotFoundException("There is no user for this id!"))
        }
//        } catch(e:NotFoundException){
//            throw(NotFoundException("There is not user for this id!"))
//        }
//        try {
//            return expenses.stream().filter { e -> e.user.id == userId }
//                .toList().map{
//                        e -> mapper.mapView(e)
//                }
//        } catch (e:NotFoundException) {
//            throw(NotFoundException(notFoundMessage))
//        }
        val expensesList = if (label.isNullOrBlank()){
            expensesRepository.findAll()
        } else {
            expensesRepository.findByexpenseLabel(label)
        }
        return expensesList
            .filter { e -> e.userCashtrack.id == userId }
            .toList()
            .map{ e -> mapper.mapView(e) }
    }
    fun register(newExpense:ExpenseEntry):ExpenseView{
        val newEntry = mapper.mapEntry(newExpense)
//        newEntry.id = (expenses.size+1).toLong()
//        expenses = expenses.plus(newEntry)
//        userService.addExpense(newEntry, newEntry.user.id)
        expensesRepository.save(newEntry)
        return mapper.mapView(newEntry)
    }
    fun update(updatedExpense:ExpenseUpdate):ExpenseView{
//        val current = expenses.stream().filter{ e -> e.id == updatedExpense.id }
//            .findFirst()
//            .orElseThrow{ NotFoundException(notFoundMessage) }
//        val update = Expense(
//            id = current.id,
//            expenseLabel = updatedExpense.expenseLabel,
//            value = updatedExpense.value,
//            type = updatedExpense.type,
//            user = userService.exportUserById(current.user.id)
//        )
//        expenses = expenses.minus(current).plus(update)
        val update = expensesRepository.getReferenceById(updatedExpense.id)
        update.expenseLabel = updatedExpense.expenseLabel
        update.value = updatedExpense.value
        update.type = updatedExpense.type
        return mapper.mapView(update)
    }
    fun delete(id:Long) {
//        val deletedExpense = expenses.stream().filter { e -> e.id == id }
//            .findFirst()
//            .orElseThrow{ NotFoundException(notFoundMessage) }
//        expenses = expenses.minus(deletedExpense)
//        userService.deleteExpense(deletedExpense)
        val deletedExpense = expensesRepository.getReferenceById(id)
        expensesRepository.delete(deletedExpense)
    }
}