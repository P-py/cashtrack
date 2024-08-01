package com.pedrosant.cashtrack.models

import com.pedrosant.cashtrack.dtos.UserView

object UserViewTest {
    fun build() = UserView(
        id = 1,
        username = "username test"
    )
}