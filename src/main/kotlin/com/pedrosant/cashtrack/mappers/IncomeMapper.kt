package com.pedrosant.cashtrack.mappers

import com.pedrosant.cashtrack.dtos.IncomeEntry
import com.pedrosant.cashtrack.dtos.IncomeView
import com.pedrosant.cashtrack.models.Income
import org.springframework.stereotype.Component

@Component
class IncomeMapper:Mapper<Income, IncomeView, IncomeEntry> {
    override fun mapView(c:Income):IncomeView {
        return IncomeView(
            incomeLabel = c.incomeLabel,
            value = c.value,
            type = c.type,
            dateCreated = c.dateCreated
        )
    }
    override fun mapEntry(e:IncomeEntry):Income {
        return Income(
            incomeLabel = e.incomeLabel,
            value = e.value,
            type = e.type,
            userId = e.userId
        )
    }
}