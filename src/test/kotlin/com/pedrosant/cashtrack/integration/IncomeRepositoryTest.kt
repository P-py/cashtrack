package com.pedrosant.cashtrack.integration

import com.pedrosant.cashtrack.models.Income
import com.pedrosant.cashtrack.models.IncomeTest
import com.pedrosant.cashtrack.models.UserCashtrackTest
import com.pedrosant.cashtrack.repository.IncomeRepository
import com.pedrosant.cashtrack.repository.UserRepository
import io.mockk.awaits
import io.mockk.every
import io.mockk.mockk
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
class IncomeRepositoryTest {
    @Autowired
    private lateinit var incomeRepository:IncomeRepository
    @Autowired
    private lateinit var userRepository:UserRepository
    private val incomeMock = IncomeTest.build()
    private val userMock = UserCashtrackTest.build()
    private val mockPageable = PageRequest.of(0, 5)

    companion object {
        @Container
        private val postgresqlContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("testDb")
            withUsername("tester")
            withPassword("pass123@tester")
        }
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry){
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
        }
    }

//    @Test
//    fun `#getByLabel should return a list of Incomes`(){
//        userRepository.save(userMock)
//        incomeRepository.save(incomeMock)
//        val incomeListReturn = incomeRepository.getByLabel(incomeMock.incomeLabel)
//
//        assertThat(incomeListReturn).isNotNull
//        assertThat(incomeListReturn).isExactlyInstanceOf(ArrayList::class.java)
//    }

    @Test
    fun `#getByLabel should return an empty list when label does not match any income`(){
        val incomeListReturn = incomeRepository.getByLabel(incomeMock.incomeLabel)

        assertThat(incomeListReturn).isEmpty()
        assertThat(incomeListReturn).isExactlyInstanceOf(ArrayList::class.java)
    }

    @Test
    fun `#findAll should return a page of Incomes class`(){
        userRepository.save(userMock)
        incomeRepository.save(incomeMock)
        val incomePageReturn = incomeRepository.findAll(mockPageable)

        assertThat(incomePageReturn).isNotNull
        assertThat(incomePageReturn.totalElements).isEqualTo(1)
        assertThat(incomePageReturn).isExactlyInstanceOf(PageImpl::class.java)
    }

    @Test
    fun `#findAll should return an empty page of Incomes class`(){
        val incomePageReturn = incomeRepository.findAll(mockPageable)

        assertThat(incomePageReturn).isNotNull
        assertThat(incomePageReturn.totalElements).isEqualTo(0)
        assertThat(incomePageReturn).isExactlyInstanceOf(PageImpl::class.java)
    }

//    @Test
//    fun `#getReferenceById should return an instance of Income class`(){
//        incomeRepository.save(incomeMock)
//        val incomeReturn = incomeRepository.getReferenceById(incomeMock.id!!)
//
//        assertThat(incomeReturn).isNotNull
//        assertThat(incomeReturn).isExactlyInstanceOf(Income::class.java)
//    }

    @Test
    fun `#getReferenceById should throw JpaObjectRetrievalFailureException when trying to retrieve a object that does not exist`(){
        assertThrows<JpaObjectRetrievalFailureException> {
            incomeRepository.getReferenceById(23)
        }
    }
}