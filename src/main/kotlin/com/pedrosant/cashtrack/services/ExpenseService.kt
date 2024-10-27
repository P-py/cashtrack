package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.ExpenseEntry
import com.pedrosant.cashtrack.dtos.ExpenseUpdate
import com.pedrosant.cashtrack.dtos.ExpenseView
import com.pedrosant.cashtrack.exceptions.AccessDeniedException
import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.extensions.extractTokenValue
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
    private val userRepository: UserRepository,
    private val tokenService: TokenService
    ){

    fun getExpenses(pageable:Pageable):Page<ExpenseView> {
        try {
            return expensesRepository.findAll(pageable)
                .map { e -> mapper.mapView(e) }
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun getExpenseById(id:Long, accessToken:String):ExpenseView {
        val userId = userRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
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

    fun getExpensesByUser(accessToken:String, label:String?):List<ExpenseView> {
        val userId = userRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
        try {
            userService.getUserById(accessToken, userId)
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

    fun register(newExpense:ExpenseEntry, accessToken: String):ExpenseView{
        val userEmail = tokenService.extractEmail(accessToken.extractTokenValue())
        val newEntry = mapper.mapEntry(newExpense)
        val user = userRepository.findByEmail(userEmail)
        newEntry.userCashtrack = user
        expensesRepository.save(newEntry)
        return mapper.mapView(newEntry)
    }

    fun update(updatedExpense:ExpenseUpdate, accessToken: String):ExpenseView{
        val userId = userRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
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

    fun delete(id:Long, accessToken: String) {
        val userId = userRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
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