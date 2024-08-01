package com.pedrosant.cashtrack

import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.exceptions.UserAlreadyExistsException
import com.pedrosant.cashtrack.mappers.UserMapper
import com.pedrosant.cashtrack.models.UserCashtrackTest
import com.pedrosant.cashtrack.models.UserEntryTest
import com.pedrosant.cashtrack.models.UserUpdateTest
import com.pedrosant.cashtrack.models.UserViewTest
import com.pedrosant.cashtrack.repository.UserRepository
import com.pedrosant.cashtrack.services.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException

class UserServiceTest{
    private val mockUsersPage = PageImpl(listOf(UserCashtrackTest.build()))
    private val mockPageable:Pageable = mockk()
    private val mockUsers = listOf(UserCashtrackTest.build())

    private val mockUserRepository:UserRepository = mockk{
        every { findAll(mockPageable) } returns mockUsersPage
        every { findAll() } returns mockUsers
    }
    private val mockMapper:UserMapper = mockk{
        every { mapView(any()) } returns UserViewTest.build()
        every { mapEntry(any()) } returns UserCashtrackTest.build()
    }

    private val userService = UserService(
        mockUserRepository, mockMapper
    )

    @Test
    fun `should return all users as a mutable list when calling findAll with no arguments`(){
        userService.getUsers(mockPageable)
        verify(exactly = 1) { mockUserRepository.findAll(mockPageable) }
        verify(exactly = 1) { mockMapper.mapView(any()) }
    }
    @Test
    fun `should return NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockUserRepository.findAll(mockPageable) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val funcReturn = assertThrows<NotFoundException> {
            userService.getUsers(mockPageable)
        }
        assertThat(funcReturn.message).isEqualTo("Oh, something went wrong!! User not found!")
    }
    @Test
    fun `should return a userView based on id`(){
        every { mockUserRepository.getReferenceById(any()) } returns UserCashtrackTest.build()

        userService.getUserById(1)
        verify(exactly = 1){ mockMapper.mapView(any()) }
        verify(exactly = 1){ mockUserRepository.getReferenceById(any()) }
    }
    @Test
    fun `getUserById should return NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockUserRepository.getReferenceById(any()) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val funcReturn = assertThrows<NotFoundException> {
            userService.getUserById(1)
        }
        assertThat(funcReturn.message).isEqualTo("Oh, something went wrong!! User not found!")
    }
    @Test
    fun `registerNewUser should return a UserView if database insertion was successfull`(){
        every { mockUserRepository.findByEmail(any()) } returns null
        every { mockUserRepository.save(any()) } returns UserCashtrackTest.build()

        userService.registerNewUser(UserEntryTest.build())
        verify(exactly = 1){ mockMapper.mapEntry(any()) }
        verify(exactly = 1){ mockUserRepository.findByEmail(any()) }
        verify(exactly = 1){ mockUserRepository.save(any()) }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `registerNewUser should return UserAlreadyExistsException if findByEmail return is not null`(){
        every { mockUserRepository.findByEmail(any()) } returns UserCashtrackTest.build()
        val exception = assertThrows<UserAlreadyExistsException> {
            userService.registerNewUser(UserEntryTest.build())
        }
        verify(exactly = 1){ mockMapper.mapEntry(any()) }
        verify(exactly = 1){ mockUserRepository.findByEmail(any()) }
        verify(exactly = 0){ mockUserRepository.save(any()) }
        verify(exactly = 0){ mockMapper.mapView(any()) }
        assertThat(exception.message).isEqualTo("An user for this e-mail already exists.")
    }
    @Test
    fun `getBalance should return Double value`(){
        every { mockUserRepository.getReferenceById(any()) } returns UserCashtrackTest.build()
        userService.getBalance(1)
        verify(exactly = 2){ mockUserRepository.getReferenceById(any()) }
    }
    @Test
    fun `getBalance should return NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockUserRepository.getReferenceById(any()) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val funcReturn = assertThrows<NotFoundException> {
            userService.getBalance(1)
        }
        assertThat(funcReturn.message).isEqualTo("Oh, something went wrong!! User not found!")
    }
    @Test
    fun `updateUser should return UserView in case of successfull update`(){
        every { mockUserRepository.getReferenceById(any()) } returns UserCashtrackTest.build()
        userService.updateUser(UserUpdateTest.build())
        verify(exactly = 1){ mockUserRepository.getReferenceById(any()) }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `updateUser should throw NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockUserRepository.getReferenceById(any()) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val funcReturn = assertThrows<NotFoundException> {
            userService.updateUser(UserUpdateTest.build())
        }
        verify(exactly = 0){ mockMapper.mapView(any()) }
        verify(exactly = 1){ mockUserRepository.getReferenceById(any()) }
        assertThat(funcReturn.message).isEqualTo("Oh, something went wrong!! User not found!")
    }
    @Test
    fun `delete shouldn't have return in case of success`(){
        every { mockUserRepository.getReferenceById(any()) } returns UserCashtrackTest.build()
        every { mockUserRepository.delete(any()) } returns Unit
        userService.delete(1)
        verify(exactly = 1){ mockUserRepository.getReferenceById(any()) }
        verify(exactly = 1){ mockUserRepository.delete(any()) }
    }
}