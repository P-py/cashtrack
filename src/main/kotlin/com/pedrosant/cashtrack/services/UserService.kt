package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.UserEntry
import com.pedrosant.cashtrack.dtos.UserUpdate
import com.pedrosant.cashtrack.dtos.UserView
import com.pedrosant.cashtrack.mappers.UserMapper
import com.pedrosant.cashtrack.models.Expense
import com.pedrosant.cashtrack.models.Income
import com.pedrosant.cashtrack.models.User
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import kotlin.math.exp

@Service
class UserService(
    private var users:List<User> = ArrayList(),
    private val mapper:UserMapper
    ){
    fun getUsers():List<UserView>{
        return users.stream().map{
            u -> mapper.mapView(u)
        }.collect(Collectors.toList())
    }
    fun getUserById(id:Long): UserView {
        val result = users.stream().filter{ u -> u.id == id }
            .findFirst().get()
        return mapper.mapView(result)
    }
    fun registerNewUser(newUser:UserEntry) {
        val new = mapper.mapEntry(newUser)
        new.id = (users.size+1).toLong()
        users = users.plus(new)
    }
    fun getBalance(userId:Long):Double {
        var totalIncome = 0.0
        var totalExpense = 0.0
        val result = users.stream().filter{ u -> u.id == userId }
            .findFirst().get()
        result.expenseList.forEach {
            e -> totalExpense += e.value
        }
        result.incomeList.forEach {
            i -> totalIncome += i.value
        }
        return totalIncome - totalExpense
    }
    fun addIncome(newIncome:Income, userId:Long){
        val user = users.stream().filter{ u -> u.id == userId }
            .findFirst()
            .get()
        val newIncomeList = user.incomeList.plus(newIncome)
        user.incomeList = newIncomeList
    }
    fun addExpense(newExpense:Expense, userId:Long){
        val user = users.stream().filter{ u -> u.id == userId }
            .findFirst()
            .get()
        val newExpenseList = user.expenseList.plus(newExpense)
        user.expenseList = newExpenseList
    }
    fun updateUser(updatedUser:UserUpdate) {
        val current = users.stream().filter {
            u -> u.id == updatedUser.userId
        }.findFirst().get()
        val update = User(
            id = current.id,
            username = updatedUser.username,
            email = updatedUser.email,
            expenseList = current.expenseList,
            incomeList = current.incomeList
        )
        users = users.minus(current).plus(update)
    }
}