package com.pedrosant.cashtrack.mappers

import com.pedrosant.cashtrack.dtos.ExpenseEntry
import com.pedrosant.cashtrack.dtos.ExpenseView
import com.pedrosant.cashtrack.models.Expense
import com.pedrosant.cashtrack.services.ExpenseService
import org.springframework.stereotype.Component

@Component
class ExpenseMapper():Mapper<Expense, ExpenseView, ExpenseEntry> {
    override fun mapEntry(e:ExpenseEntry):Expense {
        return Expense(
            expenseLabel = e.expenseLabel,
            value = e.value,
            type = e.type,
            userId = e.userId
        )
    }
    override fun mapView(c:Expense):ExpenseView {
        return ExpenseView(
            id = c.id,
            expenseLabel = c.expenseLabel,
            value = c.value,
            type = c.type,
            dateCreated = c.dateCreated
        )
    }
}