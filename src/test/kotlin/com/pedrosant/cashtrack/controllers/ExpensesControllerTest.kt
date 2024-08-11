package com.pedrosant.cashtrack.controllers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExpensesControllerTest {
    @Autowired
    private lateinit var webApplicationContext:WebApplicationContext
    private lateinit var mockMvc:MockMvc

    companion object{
        private const val HOST = "/expenses"
    }

    @BeforeEach
    fun setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    @Test
    fun `GET request to admin list should return a 403 forbidden when there is no auth and admin role`(){
        mockMvc.get("$HOST/admin-list").andExpect { status{ isForbidden() } }
    }

    @Test
    fun `GET request to incomes should return a 403 forbidden when there is no auth`(){
        mockMvc.get(HOST).andExpect { status{ isForbidden() } }
    }

    @Test
    fun `GET request to specific income with id should return a 403 forbidden when there is no auth`(){
        mockMvc.get("$HOST/1").andExpect { status{ isForbidden() } }
    }
}