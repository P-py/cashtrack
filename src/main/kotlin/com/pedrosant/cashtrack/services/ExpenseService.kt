package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.ExpenseEntry
import com.pedrosant.cashtrack.dtos.ExpenseUpdate
import com.pedrosant.cashtrack.dtos.ExpenseView
import com.pedrosant.cashtrack.exceptions.AccessDeniedException
import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.mappers.ExpenseMapper
import com.pedrosant.cashtrack.repository.ExpenseRepository
import com.pedrosant.cashtrack.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ExpenseService(
    private var expensesRepository:ExpenseRepository,
    private val mapper:ExpenseMapper,
    private val userService:UserService,
    private val notFoundMessage:String = "Oh, something went wrong! Expense not found!",
    private val userRepository: UserRepository
    ){

    fun getExpenses(pageable:Pageable):Page<ExpenseView> {
        try {
            return expensesRepository.findAll(pageable)
                .map { e -> mapper.mapView(e) }
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun getExpenseById(id:Long, userId:Long):ExpenseView {
        try {
            val expense = expensesRepository.getReferenceById(id)
            if (expense.userCashtrack!!.id == userId){
                return mapper.mapView(expensesRepository.getReferenceById(id))
            } else {
                throw AccessDeniedException("You don't have permission to access this page.")
            }
        } catch (e: JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun getExpensesByUser(userId:Long, label:String?):List<ExpenseView> {
        try {
            userService.getUserById(userId)
        } catch(e:JpaObjectRetrievalFailureException){
            throw(NotFoundException("There is no user for this id!"))
        }
        val expensesList = if (label.isNullOrBlank()){
            expensesRepository.findAll()
        } else {
            expensesRepository.getByLabel(label)
        }
        return expensesList
            .filter { e -> e.userCashtrack!!.id == userId }
            .toList()
            .map{ e -> mapper.mapView(e) }
    }
    fun register(newExpense:ExpenseEntry, userEmail: String):ExpenseView{
        val newEntry = mapper.mapEntry(newExpense)
        val user = userRepository.findByEmail(userEmail)
        newEntry.userCashtrack = user
        expensesRepository.save(newEntry)
        return mapper.mapView(newEntry)
    }

    fun update(updatedExpense:ExpenseUpdate, userId:Long):ExpenseView{
        try {
            val update = expensesRepository.getReferenceById(updatedExpense.id)
            if (update.userCashtrack!!.id == userId) {
                update.expenseLabel = updatedExpense.expenseLabel
                update.value = updatedExpense.value
                update.type = updatedExpense.type
                update.lastUpdatedAt = LocalDateTime.now()
                return mapper.mapView(update)
            } else throw AccessDeniedException("You don't have permission to access this page.")
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun delete(id:Long, userId:Long) {
        try {
            val deletedExpense = expensesRepository.getReferenceById(id)
            if (deletedExpense.userCashtrack!!.id == userId){
                expensesRepository.delete(deletedExpense)
            } else throw AccessDeniedException("You don't have access to this page.")
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException("You can't delete a user that does not exist!"))
        }
    }
}