package com.pedrosant.cashtrack.mappers

import com.pedrosant.cashtrack.dtos.IncomeEntry
import com.pedrosant.cashtrack.dtos.IncomeView
import com.pedrosant.cashtrack.models.Income
import com.pedrosant.cashtrack.services.UserService
import org.springframework.stereotype.Component

@Component
class IncomeMapper(private val userService:UserService):Mapper<Income, IncomeView, IncomeEntry> {
    override fun mapView(c:Income):IncomeView {
        return IncomeView(
            id = c.id,
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
            userCashtrack = userService.exportUserById(e.userId)
        )
    }
}