package com.pedrosant.cashtrack.repository

import com.pedrosant.cashtrack.models.Income
import org.springframework.data.jpa.repository.JpaRepository

interface IncomeRepository:JpaRepository<Income, Long>{
    fun findByincomeLabel(label:String):List<Income>
}