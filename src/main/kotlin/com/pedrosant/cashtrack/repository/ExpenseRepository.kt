package com.pedrosant.cashtrack.repository

import com.pedrosant.cashtrack.models.Expense
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ExpenseRepository:JpaRepository<Expense, Long>{
    @Query(
        value = "SELECT * FROM expense WHERE expense_label = :label",
        nativeQuery = true
    )
    fun getByLabel(@Param("label") label:String):List<Expense>
}