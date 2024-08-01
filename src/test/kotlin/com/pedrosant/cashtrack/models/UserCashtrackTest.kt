package com.pedrosant.cashtrack.models

object UserCashtrackTest {
    fun build() = UserCashtrack(
        id = 1,
        username = "Test username",
        email = "test@test.com",
        password = "t3st123",
        role = RoleTest.build(),
    )
}