package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.mappers.IncomeMapper
import com.pedrosant.cashtrack.models.*
import com.pedrosant.cashtrack.repository.IncomeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException

class IncomeServiceTest {
    private val mockPageIncomes = PageImpl(listOf(IncomeTest.build()))
    private val mockIncomes = listOf(IncomeTest.build())
    private val mockPageable:Pageable = mockk()
    private val mockIncomesRepository:IncomeRepository = mockk{
        every { findAll(mockPageable) } returns mockPageIncomes
        every { findAll() } returns mockIncomes
        every { getReferenceById(any()) } returns IncomeTest.build()
        every { getByLabel(any()) } returns listOf(IncomeTest.build())
        every { save(any()) } returns IncomeTest.build()
    }
    private val mockMapper:IncomeMapper = mockk{
        every { mapView(any()) } returns IncomeViewTest.build()
        every { mapEntry(any()) } returns IncomeTest.build()
    }
    private val mockUserService:UserService = mockk{
        every { getUserById(any()) } returns UserViewTest.build()
    }
    private val defaultNotFoundMessage = "Oh, something went wrong! Income not found!"

    private val incomeService = IncomeService(
        mockIncomesRepository,
        mockMapper,
        mockUserService
    )

    @Test
    fun `getIncomes should return Page of IncomeView in case of success`(){
        incomeService.getIncomes(mockPageable)
        verify(exactly = 1){ mockIncomesRepository.findAll(mockPageable) }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getIncomes should throw NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockIncomesRepository.findAll(mockPageable) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val exception = assertThrows<NotFoundException> {
            incomeService.getIncomes(mockPageable)
        }
        assertThat(exception.message).isEqualTo(defaultNotFoundMessage)
    }
    @Test
    fun `getIncomeById should return IncomeView in case of success`(){
        incomeService.getIncomeById(1, 1)
        verify(exactly = 2){ mockIncomesRepository.getReferenceById(any()) }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getIncomeById should throw NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockIncomesRepository.getReferenceById(any()) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val exception = assertThrows<NotFoundException> {
            incomeService.getIncomeById(1, 1)
        }
        assertThat(exception.message).isEqualTo(defaultNotFoundMessage)
        verify(exactly = 1){ mockIncomesRepository.getReferenceById(any()) }
        verify(exactly = 0){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getIncomesByUser should return List of IncomeView in case of success - with label`(){
        incomeService.getIncomesByUser(1, "tester")
        verify(exactly = 1){ mockUserService.getUserById(1) }
        verify(exactly = 1){ mockIncomesRepository.getByLabel("tester") }
        verify(exactly = 0){ mockIncomesRepository.findAll() }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getIncomesByUser should use findAll if label is null`(){
        incomeService.getIncomesByUser(1, null)
        verify(exactly = 1){ mockUserService.getUserById(1) }
        verify(exactly = 0){ mockIncomesRepository.getByLabel(any()) }
        verify(exactly = 1){ mockIncomesRepository.findAll() }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `getIncomesByUser should throw NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockUserService.getUserById(any()) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val exception = assertThrows<NotFoundException> {
            incomeService.getIncomesByUser(1, null)
        }
        assertThat(exception.message).isEqualTo("There is no user for this id!")
        verify(exactly = 1){ mockUserService.getUserById(any()) }
        verify(exactly = 0){ mockIncomesRepository.getByLabel(any()) }
        verify(exactly = 0){ mockIncomesRepository.findAll() }
        verify(exactly = 0){ mockMapper.mapView(any()) }
    }
    @Test
    fun `register should return IncomeView in case of successfull database insertion`(){
        incomeService.register(IncomeEntryTest.build())
        verify(exactly = 1){ mockMapper.mapEntry(any()) }
        verify(exactly = 1){ mockIncomesRepository.save(any()) }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `update should return IncomeView in case of successfull update`(){
        incomeService.update(IncomeUpdateTest.build(), 1)
        verify(exactly = 1){ mockIncomesRepository.getReferenceById(1) }
        verify(exactly = 1){ mockMapper.mapView(any()) }
    }
    @Test
    fun `update should throw NotFoundException in case of JpaObjectRetrievalFailureException`(){
        every { mockIncomesRepository.getReferenceById(any()) } throws JpaObjectRetrievalFailureException(
            EntityNotFoundException()
        )
        val exception = assertThrows<NotFoundException> {
            incomeService.update(IncomeUpdateTest.build(), 1)
        }
        assertThat(exception.message).isEqualTo(defaultNotFoundMessage)
        verify(exactly = 1){ mockIncomesRepository.getReferenceById(any()) }
        verify(exactly = 0){ mockMapper.mapView(any()) }
    }
    @Test
    fun `delete shouldn't have return in case of success`(){
        incomeService.delete()
    }
    @Test
    fun `delete should throw AccessDeniedException in case of user not permitted by userId`(){
        TODO()
    }
    @Test
    fun `delete should throw NotFoundException in case of JpaObjectRetrievalException`(){
        TODO()
    }
}