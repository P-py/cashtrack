package com.pedrosant.cashtrack.models

import com.pedrosant.cashtrack.dtos.UserUpdate

object UserUpdateTest {
    fun build() = UserUpdate(
        userId = 1,
        username = "tester",
        email = "tester@test.com"
    )
}