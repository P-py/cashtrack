package com.pedrosant.cashtrack.repository

import com.pedrosant.cashtrack.models.Expense
import org.springframework.data.jpa.repository.JpaRepository

interface ExpenseRepository:JpaRepository<Expense, Long>{
    fun findByexpenseLabel(label:String):List<Expense>
}