package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.mappers.ExpenseMapper
import com.pedrosant.cashtrack.models.*
import com.pedrosant.cashtrack.repository.ExpenseRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException

class ExpenseServiceTest {
    private val mockPageExpenses = PageImpl(listOf(ExpenseTest.build()))
    private val mockExpenses = listOf(ExpenseTest.build())
    private val mockPageable:Pageable = mockk()
    private val mockExpensesRepository:ExpenseRepository = mockk{
        every { findAll(mockPageable) } returns mockPageExpenses
        every { findAll() } returns mockExpenses
        every { getReferenceById(any()) } returns ExpenseTest.build()
        every { getByLabel(any()) } returns listOf(ExpenseTest.build())
        every { save(any()) } returns ExpenseTest.build()
        every { delete(any()) } returns Unit
    }
    private val mockMapper:ExpenseMapper = mockk{
        every { mapView(any()) } returns ExpenseViewTest.build()
        every { mapEntry(any()) } returns ExpenseTest.build()
    }
    private val mockUserService: UserService = mockk{
        every { getUserById(any()) } returns UserViewTest.build()
    }
    private val defaultNotFoundMessage = "Oh, something went wrong! Expense not found!"

    private val expenseService = ExpenseService(
        mockExpensesRepository,
        mockMapper,
        mockUserService
    )

    @Test
    fun `getExpenses should return Page of ExpenseView`(){
        expenseService.getExpenses(mockPageable)
        verify(exactly = 1){ mockExpensesRepository.findAll(mockPageable) }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getExpenses should throw NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockExpensesRepository.findAll(mockPageable) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val exception = assertThrows<NotFoundException> {
            expenseService.getExpenses(mockPageable)
        }
        assertThat(exception.message).isEqualTo(defaultNotFoundMessage)
        verify(exactly = 1){ mockExpensesRepository.findAll(mockPageable) }
        verify(exactly = 0){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getExpenseById should return a single ExpenseView`(){
        expenseService.getExpenseById(1, 1)
        verify(exactly = 2){ mockExpensesRepository.getReferenceById(any()) }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getExpenseById should throw NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockExpensesRepository.getReferenceById(any()) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val exception = assertThrows<NotFoundException> {
            expenseService.getExpenseById(1, 1)
        }
        assertThat(exception.message).isEqualTo(defaultNotFoundMessage)
        verify(exactly = 1){ mockExpensesRepository.getReferenceById(any()) }
        verify(exactly = 0){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getExpensesByUser should return a List of ExpenseView - non null label`(){
        expenseService.getExpensesByUser(1, "tester")
        verify(exactly = 1){ mockUserService.getUserById(1) }
        verify(exactly = 1){ mockExpensesRepository.getByLabel("tester") }
        verify(exactly = 0){ mockExpensesRepository.findAll() }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getExpensesByUser should return a List of ExpenseView - using findAll with null label`(){
        expenseService.getExpensesByUser(1, null)
        verify(exactly = 1){ mockUserService.getUserById(1) }
        verify(exactly = 0){ mockExpensesRepository.getByLabel(any()) }
        verify(exactly = 1){ mockExpensesRepository.findAll() }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getExpensesByUser should throw NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockUserService.getUserById(any()) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val exception = assertThrows<NotFoundException> {
            expenseService.getExpensesByUser(1, "tester")
        }
        assertThat(exception.message).isEqualTo("There is no user for this id!")
        verify(exactly = 1){ mockUserService.getUserById(1) }
        verify(exactly = 0){ mockExpensesRepository.findAll() }
        verify(exactly = 0){ mockExpensesRepository.getByLabel(any()) }
        verify(exactly = 0){ mockMapper.mapView(any()) }
    }
    @Test
    fun `update should return ExpenseView`(){
        expenseService.update(ExpenseUpdateTest.build(), 1)
        verify(exactly = 1){ mockExpensesRepository.getReferenceById(1) }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `update should throw NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every{ mockExpensesRepository.getReferenceById(any()) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val exception = assertThrows<NotFoundException>{
            expenseService.update(ExpenseUpdateTest.build(),  1)
        }
        assertThat(exception.message).isEqualTo(defaultNotFoundMessage)
        verify(exactly = 1){ mockExpensesRepository.getReferenceById(any()) }
        verify(exactly = 0){ mockMapper.mapView(any()) }
    }
    @Test
    fun `delete shouldn't have return`(){
        expenseService.delete(1, 1)
        verify(exactly = 1){ mockExpensesRepository.getReferenceById(1) }
        verify(exactly = 1){ mockExpensesRepository.delete(any()) }
    }
    @Test
    fun `delete should throw NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockExpensesRepository.getReferenceById(any()) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val exception = assertThrows<NotFoundException>{
            expenseService.delete(1, 1)
        }
        assertThat(exception.message).isEqualTo("You can't delete a user that does not exist!")
        verify(exactly = 1){ mockExpensesRepository.getReferenceById(1) }
        verify(exactly = 0){ mockExpensesRepository.delete(any()) }
    }
}