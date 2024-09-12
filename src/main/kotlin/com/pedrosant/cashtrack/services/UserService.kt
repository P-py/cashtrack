package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.UserEntry
import com.pedrosant.cashtrack.dtos.UserUpdate
import com.pedrosant.cashtrack.dtos.UserView
import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.exceptions.UserAlreadyExistsException
import com.pedrosant.cashtrack.mappers.UserMapper
import com.pedrosant.cashtrack.models.Expense
import com.pedrosant.cashtrack.models.Income
import com.pedrosant.cashtrack.models.UserCashtrack
import com.pedrosant.cashtrack.repository.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val usersRepository:UserRepository,
    private val mapper:UserMapper,
    private val notFoundMessage:String = "Oh, something went wrong!! User not found!"
    ) {

    @Cacheable(cacheNames = ["UsersList"], key = "#root.method.name")
    fun getUsers(pageable:Pageable):Page<UserView> {
//        return users.stream().map{
//            u -> mapper.mapView(u)
//        }.collect(Collectors.toList())
//        return usersRepository.findAll().map{
//            u -> mapper.mapView(u)
//        }
        try {
            return usersRepository.findAll(pageable)
                .map{ u -> mapper.mapView(u) }
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    @Cacheable(cacheNames = ["UserDetails"], key = "#root.method.name")
    fun getUserById(id:Long):UserView {
//        val result = users.stream().filter{ u -> u.id == id }
//            .findFirst().orElseThrow{ NotFoundException(notFoundMessage) }
//        return mapper.mapView(result)
        try {
            return mapper.mapView(usersRepository.getReferenceById(id))
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun exportUserById(id:Long):UserCashtrack {
        return usersRepository.getReferenceById(id)
    }

    @Caching(evict = [CacheEvict("UsersList", allEntries = true), CacheEvict("UserDetails", allEntries = true)])
    fun registerNewUser(newUser:UserEntry):UserView {
        val new = mapper.mapEntry(newUser)
        if(usersRepository.findByEmail(new.email) == null) {
            usersRepository.save(new)
            return mapper.mapView(new)
        } else throw UserAlreadyExistsException("An user for this e-mail already exists.")
//        new.id = (users.size+1).toLong()
//        users = users.plus(new)
    }

    @Cacheable(cacheNames = ["Balance"], key = "#root.method.name")
    fun getBalance(userId:Long):Double {
//        var totalIncome = 0.0
//        var totalExpense = 0.0
//        val result = users.stream().filter{ u -> u.id == userId }
//            .findFirst().orElseThrow{ NotFoundException(notFoundMessage) }
//        result.expenseList.forEach {
//            e -> totalExpense += e.value
//        }
//        result.incomeList.forEach {
//            i -> totalIncome += i.value
//        }
//        return totalIncome - totalExpense
        try {
            val userIncomeList = usersRepository.getReferenceById(userId)
                .incomeList
                .sumOf { i -> i.value }
            val userExpenseList = usersRepository.getReferenceById(userId)
                .expenseList
                .sumOf { i -> i.value }
            return userIncomeList - userExpenseList
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun addIncome(newIncome:Income, userId:Long) {
//        val user = users.stream().filter{ u -> u.id == userId }
//            .findFirst()
//            .orElseThrow{ NotFoundException(notFoundMessage) }
//        val newIncomeList = user.incomeList.plus(newIncome)
//        user.incomeList = newIncomeList
        usersRepository.getReferenceById(userId).incomeList.plus(newIncome)
    }

    fun addExpense(newExpense:Expense, userId:Long) {
//        val user = users.stream().filter{ u -> u.id == userId }
//            .findFirst()
//            .orElseThrow{ NotFoundException(notFoundMessage) }
//        val newExpenseList = user.expenseList.plus(newExpense)
//        user.expenseList = newExpenseList
        usersRepository.getReferenceById(userId).expenseList.plus(newExpense)
    }

    @Caching(evict = [CacheEvict("UsersList", allEntries = true), CacheEvict("UserDetails", allEntries = true)])
    fun updateUser(updatedUser:UserUpdate):UserView {
//        val current = users.stream().filter {
//            u -> u.id == updatedUser.userId
//        }.findFirst().orElseThrow{ NotFoundException(notFoundMessage) }
//        val update = User(
//            id = current.id,
//            username = updatedUser.username,
//            email = updatedUser.email,
//            expenseList = current.expenseList,
//            incomeList = current.incomeList
//        )
//        users = users.minus(current).plus(update)
        try {
            val update = usersRepository.getReferenceById(updatedUser.userId)
            update.username = updatedUser.username
            update.email = updatedUser.email
            return mapper.mapView(update)
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun updateExpense(updatedExpense:Expense, currentExpense:Expense) {
//        val user = users.stream().filter {
//            u -> u.id == updatedExpense.userId
//        }.findFirst().orElseThrow{ NotFoundException(notFoundMessage) }
//        val updatedExpenseList = user.expenseList
//            .minus(currentExpense)
//            .plus(updatedExpense)
//        user.expenseList = updatedExpenseList
//        val user = usersRepository.getReferenceById(updatedExpense.user.id)
//        user.expenseList.minus(currentExpense).plus(updatedExpense)
        if (updatedExpense.userCashtrack.id == null){
            throw(NotFoundException(notFoundMessage))
        } else {
            val user = usersRepository.getReferenceById(updatedExpense.userCashtrack.id!!)
            user.expenseList.minus(currentExpense).plus(updatedExpense)
        }
    }

    fun updateIncome(updatedIncome:Income, currentIncome:Income) {
//        val user = users.stream().filter {
//                u -> u.id == updatedIncome.userId
//        }.findFirst().orElseThrow{ NotFoundException(notFoundMessage) }
//        val updatedIncomeList = user.incomeList
//            .minus(currentIncome)
//            .plus(updatedIncome)
//        user.incomeList = updatedIncomeList
        if (updatedIncome.userCashtrack.id == null){
            throw(NotFoundException(notFoundMessage))
        } else {
            val user = usersRepository.getReferenceById(updatedIncome.userCashtrack.id!!)
            user.incomeList.minus(currentIncome).plus(updatedIncome)
        }
    }

    @Caching(evict = [CacheEvict("UsersList", allEntries = true), CacheEvict("UserDetails", allEntries = true)])
    fun delete(id:Long) {
//        val deletedUser = users.stream().filter { u -> u.id == id }
//            .findFirst()
//            .orElseThrow{ NotFoundException(notFoundMessage) }
//        users = users.minus(deletedUser)
        try {
            val deletedUser = usersRepository.getReferenceById(id)
            usersRepository.delete(deletedUser)
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException("You can't delete a user that does not exist!"))
        }
    }

    fun deleteExpense(deletedExpense:Expense) {
//        val user = users.stream().filter { u -> u.id == deletedExpense.userId }
//            .findFirst().orElseThrow{ NotFoundException(notFoundMessage) }
//        user.expenseList = user.expenseList.minus(deletedExpense)
        if (deletedExpense.userCashtrack.id == null){
            throw(NotFoundException(notFoundMessage))
        } else {
            val user = usersRepository.getReferenceById(deletedExpense.userCashtrack.id!!)
            user.expenseList.minus(deletedExpense)
        }
    }

    fun deleteIncome(deletedIncome:Income) {
//        val user = users.stream().filter { u -> u.id == deletedIncome.userId }
//            .findFirst().orElseThrow{ NotFoundException(notFoundMessage) }
//        user.incomeList = user.incomeList.minus(deletedIncome)
        if (deletedIncome.userCashtrack.id == null){
            throw(NotFoundException(notFoundMessage))
        } else {
            val user = usersRepository.getReferenceById(deletedIncome.userCashtrack.id!!)
            user.incomeList.minus(deletedIncome)
        }
    }
}