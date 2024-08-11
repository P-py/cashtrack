package com.pedrosant.cashtrack.integration

import com.pedrosant.cashtrack.models.UserCashtrack
import com.pedrosant.cashtrack.models.UserCashtrackTest
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
// annotation used to apply this database only for test purpose
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    // lateinit variables are only initialized when called and not @ compile time
    @Autowired
    private lateinit var userRepository:UserRepository
    private val userMock = UserCashtrackTest.build()
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
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
        }
    }

    @Test
    fun `#findByEmail should return a UserCashtrackInstance`(){
        userRepository.save(userMock)
        val userReturn = userRepository.findByEmail(userMock.email)

        assertThat(userReturn).isNotNull
        assertThat(userReturn).isExactlyInstanceOf(UserCashtrack::class.java)
    }

    @Test
    fun `#findByEmail should return null when called with a email that does not correspond to a user`(){
        userRepository.save(userMock)
        val userReturn = userRepository.findByEmail("invalidEmail@test.com")

        assertThat(userReturn).isNull()
    }

    @Test
    fun `#findAll should return a Page of UserView`(){
        userRepository.save(userMock)
        val pageReturn = userRepository.findAll(mockPageable)

        assertThat(pageReturn.totalElements).isEqualTo(1)
        assertThat(pageReturn).isExactlyInstanceOf(PageImpl::class.java)
    }

    @Test
    fun `#findAll should return a empty Page of UserView`(){
        val pageReturn = userRepository.findAll(mockPageable)

        assertThat(pageReturn.totalElements).isEqualTo(0)
        assertThat(pageReturn).isExactlyInstanceOf(PageImpl::class.java)
    }

    @Test
    fun `#getReferenceById should return an instance of UserCashtrack`(){
        userRepository.save(userMock)
        val userReturn = userRepository.getReferenceById(userMock.id!!)

        assertThat(userReturn).isNotNull
        assertThat(userReturn).isExactlyInstanceOf(UserCashtrack::class.java)
    }

    @Test
    fun `#getReferenceById should throw JpaObjectRetrievalFailureException when query is not successful`(){
        assertThrows<JpaObjectRetrievalFailureException> {
            userRepository.getReferenceById(1)
        }
    }
}