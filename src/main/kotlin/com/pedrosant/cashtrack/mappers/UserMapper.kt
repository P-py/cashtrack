package com.pedrosant.cashtrack.mappers

import com.pedrosant.cashtrack.dtos.UserEntry
import com.pedrosant.cashtrack.dtos.UserView
import com.pedrosant.cashtrack.models.UserCashtrack
import org.springframework.stereotype.Component

@Component
class UserMapper:Mapper<UserCashtrack, UserView, UserEntry>{
    override fun mapView(c:UserCashtrack):UserView {
        return UserView(
            id = c.id,
            username = c.username
        )
    }
    override fun mapEntry(e:UserEntry):UserCashtrack {
        return UserCashtrack(
            username = e.username,
            email = e.email
        )
    }
}