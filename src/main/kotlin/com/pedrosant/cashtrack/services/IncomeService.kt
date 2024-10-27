package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.IncomeEntry
import com.pedrosant.cashtrack.dtos.IncomeUpdate
import com.pedrosant.cashtrack.dtos.IncomeView
import com.pedrosant.cashtrack.exceptions.AccessDeniedException
import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.extensions.extractTokenValue
import com.pedrosant.cashtrack.mappers.IncomeMapper
import com.pedrosant.cashtrack.repository.IncomeRepository
import com.pedrosant.cashtrack.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class IncomeService(
    private var incomesRepository:IncomeRepository,
    private val mapper:IncomeMapper,
    private val userService:UserService,
    private val notFoundMessage:String = "Oh, something went wrong! Income not found!",
    private val usersRepository:UserRepository,
    private val tokenService: TokenService
    ){

    fun getIncomes(pageable:Pageable):Page<IncomeView>{
        try {
            // different from the .findAll() default method, using the pageable argument from type
            // spring.data.domain.Pageable it returns a Page interface
            return incomesRepository.findAll(pageable)
                .map { i -> mapper.mapView(i) }
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun getIncomeById(id:Long, accessToken:String):IncomeView{
        val userId = usersRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
        try {
            val income = incomesRepository.getReferenceById(id)
            if (income.userCashtrack?.id == userId){
                return mapper.mapView(incomesRepository.getReferenceById(id))
            } else {
                throw AccessDeniedException("You don't have permission to access this page.")
            }
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun getIncomesByUser(accessToken: String, label:String?):List<IncomeView>{
        val userId = usersRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
        try {
            userService.getUserById(accessToken, userId)
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException("There is no user for this id!"))
        }
        val incomesList = if (label.isNullOrEmpty()){
            incomesRepository.findAll()
        } else {
            incomesRepository.getByLabel(label)
        }
        return incomesList
            .filter { i -> i.userCashtrack?.id == userId }
            .toList()
            .map { i -> mapper.mapView(i) }
    }

    fun register(newIncome:IncomeEntry, accessToken: String):IncomeView{
        val userEmail = tokenService.extractEmail(accessToken.extractTokenValue())
        val new = mapper.mapEntry(newIncome)
        val user = usersRepository.findByEmail(userEmail)
        new.userCashtrack = user
        incomesRepository.save(new)
        return mapper.mapView(new)
    }

    fun update(updatedIncome:IncomeUpdate, accessToken: String):IncomeView{
        val userId = usersRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
        try {
            val update = incomesRepository.getReferenceById(updatedIncome.id)
            if (update.userCashtrack?.id == userId){
                update.incomeLabel = updatedIncome.incomeLabel
                update.value = updatedIncome.value
                update.type = updatedIncome.type
                update.lastUpdatedAt = LocalDateTime.now()
                return mapper.mapView(update)
            } else throw AccessDeniedException("You don't have permission to access this page.")
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun delete(id:Long, accessToken: String){
        val userId = usersRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
        try {
            val deletedIncome = incomesRepository.getReferenceById(id)
            if (deletedIncome.userCashtrack?.id == userId){
                incomesRepository.delete(deletedIncome)
            } else throw AccessDeniedException("You don't have access to this page.")
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException("You can't delete an income that does not exist!"))
        }
    }
}