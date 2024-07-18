package com.pedrosant.cashtrack.mappers

import com.pedrosant.cashtrack.dtos.UserEntry
import com.pedrosant.cashtrack.dtos.UserUpdate
import com.pedrosant.cashtrack.dtos.UserView
import com.pedrosant.cashtrack.models.User
import org.springframework.stereotype.Component

@Component
class UserMapper:Mapper<User, UserView, UserEntry>{
    override fun mapView(c:User):UserView {
        return UserView(
            id = c.id,
            username = c.username
        )
    }
    override fun mapEntry(e:UserEntry):User {
        return User(
            username = e.username,
            email = e.email
        )
    }
}