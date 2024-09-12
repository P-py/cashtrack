package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.ExpenseEntry
import com.pedrosant.cashtrack.dtos.ExpenseUpdate
import com.pedrosant.cashtrack.dtos.ExpenseView
import com.pedrosant.cashtrack.exceptions.AccessDeniedException
import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.mappers.ExpenseMapper
import com.pedrosant.cashtrack.repository.ExpenseRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
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
    private val notFoundMessage:String = "Oh, something went wrong! Expense not found!"
    ){

    @Cacheable(cacheNames = ["Expenses"], key = "#root.method.name")
    fun getExpenses(pageable:Pageable):Page<ExpenseView> {
//        return expenses.stream().map {
//            e -> mapper.mapView(e)
//        }.collect(Collectors.toList())
        try {
            return expensesRepository.findAll(pageable)
                .map { e -> mapper.mapView(e) }
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun getExpenseById(id:Long, userId:Long):ExpenseView {
//        val result =  expenses.stream().filter { e -> e.id == id }
//            .findFirst().orElseThrow{ NotFoundException(notFoundMessage) }
        try {
            val expense = expensesRepository.getReferenceById(id)
            if (expense.userCashtrack.id == userId){
                return mapper.mapView(expensesRepository.getReferenceById(id))
            } else {
                throw AccessDeniedException("You don't have permission to access this page.")
            }
        } catch (e: JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    @Cacheable(cacheNames = ["ExpensesByUser"], key = "#root.method.name")
    fun getExpensesByUser(userId:Long, label:String?):List<ExpenseView> {
//        try {
        try {
            userService.getUserById(userId)
        } catch(e:JpaObjectRetrievalFailureException){
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
            expensesRepository.getByLabel(label)
        }
        return expensesList
            .filter { e -> e.userCashtrack.id == userId }
            .toList()
            .map{ e -> mapper.mapView(e) }
    }

    @Caching(evict = [CacheEvict("Expenses", allEntries = true),
        CacheEvict("ExpensesByUser", allEntries = true),
        CacheEvict("Balance", allEntries = true)])
    fun register(newExpense:ExpenseEntry):ExpenseView{
        val newEntry = mapper.mapEntry(newExpense)
//        newEntry.id = (expenses.size+1).toLong()
//        expenses = expenses.plus(newEntry)
//        userService.addExpense(newEntry, newEntry.user.id)
        expensesRepository.save(newEntry)
        return mapper.mapView(newEntry)
    }

    @Caching(evict = [CacheEvict("Expenses", allEntries = true),
        CacheEvict("ExpensesByUser", allEntries = true),
        CacheEvict("Balance", allEntries = true)])
    fun update(updatedExpense:ExpenseUpdate, userId:Long):ExpenseView{
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
        try {
            val update = expensesRepository.getReferenceById(updatedExpense.id)
            if (update.userCashtrack.id == userId) {
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

    @Caching(evict = [CacheEvict("Expenses", allEntries = true),
        CacheEvict("ExpensesByUser", allEntries = true),
        CacheEvict("Balance", allEntries = true)])
    fun delete(id:Long, userId:Long) {
//        val deletedExpense = expenses.stream().filter { e -> e.id == id }
//            .findFirst()
//            .orElseThrow{ NotFoundException(notFoundMessage) }
//        expenses = expenses.minus(deletedExpense)
//        userService.deleteExpense(deletedExpense)
        try {
            val deletedExpense = expensesRepository.getReferenceById(id)
            if (deletedExpense.userCashtrack.id == userId){
                expensesRepository.delete(deletedExpense)
            } else throw AccessDeniedException("You don't have access to this page.")
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException("You can't delete a user that does not exist!"))
        }
    }
}