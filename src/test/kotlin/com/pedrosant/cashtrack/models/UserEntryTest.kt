package com.pedrosant.cashtrack.models

import com.pedrosant.cashtrack.dtos.UserEntry

object UserEntryTest {
    fun build() = UserEntry(
        email = "testuser@test.com",
        username = "tester",
        password = "test123"
    )
}