package com.pedrosant.cashtrack.integration

import com.pedrosant.cashtrack.models.Expense
import com.pedrosant.cashtrack.models.ExpenseTest
import com.pedrosant.cashtrack.models.UserCashtrackTest
import com.pedrosant.cashtrack.repository.ExpenseRepository
import com.pedrosant.cashtrack.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExpenseRepositoryTest {
    @Autowired
    private lateinit var expenseRepository:ExpenseRepository
    @Autowired
    private lateinit var userRepository:UserRepository
    private val expenseMock = ExpenseTest.build()
    private val mockPageable = PageRequest.of(0, 5)

    companion object {
        @Container
        private val postgresqlContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply{
            withDatabaseName("testDb")
            withUsername("tester")
            withPassword("pass123@tester")
        }
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry:DynamicPropertyRegistry){
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
        }
    }

//    @Test
//    fun `#getByLabel should return a list of Expenses`(){
//        userRepository.save(UserCashtrackTest.build())
//        expenseRepository.save(expenseMock)
//        val expenseListReturn = expenseRepository.getByLabel(expenseMock.expenseLabel)
//
//        assertThat(expenseListReturn).isNotNull
//        assertThat(expenseListReturn).isExactlyInstanceOf(ArrayList::class.java)
//    }

    @Test
    fun `#getByLabel should return an empty list when label does not match any expense`(){
        val expenseListReturn = expenseRepository.getByLabel(expenseMock.expenseLabel)

        assertThat(expenseListReturn).isEmpty()
        assertThat(expenseListReturn).isExactlyInstanceOf(ArrayList::class.java)
    }

//    @Test
//    fun `#findAll should return a page of Expense class`(){
//        userRepository.save(UserCashtrackTest.build())
//        expenseRepository.save(expenseMock)
//        val expensePageReturn = expenseRepository.findAll(mockPageable)
//
//        assertThat(expensePageReturn).isNotNull
//        assertThat(expensePageReturn.totalElements).isEqualTo(1)
//        assertThat(expensePageReturn).isExactlyInstanceOf(PageImpl::class.java)
//    }

    @Test
    fun `#findAll should return an empty page of Expense class`(){
        val expensePageReturn = expenseRepository.findAll(mockPageable)

        assertThat(expensePageReturn).isNotNull
        assertThat(expensePageReturn.totalElements).isEqualTo(0)
        assertThat(expensePageReturn).isExactlyInstanceOf(PageImpl::class.java)
    }

    @Test
    fun `#getReferenceById should return an instance of expense class`(){
        userRepository.save(UserCashtrackTest.build())
        expenseRepository.save(expenseMock)
        val expenseReturn = expenseRepository.getReferenceById(expenseMock.id!!)

        assertThat(expenseReturn).isNotNull
        assertThat(expenseReturn).isExactlyInstanceOf(Expense::class.java)
    }

    @Test
    fun `#getReferenceById should throw JpaObjectRetrievalFailureException when trying to retrieve a object that does not exist`(){
        assertThrows<JpaObjectRetrievalFailureException> {
            expenseRepository.getReferenceById(23)
        }
    }
}