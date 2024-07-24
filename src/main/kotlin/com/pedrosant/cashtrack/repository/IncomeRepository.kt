package com.pedrosant.cashtrack.repository

import com.pedrosant.cashtrack.models.Income
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface IncomeRepository:JpaRepository<Income, Long>{
    @Query(
        value = "SELECT * FROM income WHERE income_label = :label",
        nativeQuery = true
    )
    fun getByLabel(@Param("label") label:String):List<Income>
}