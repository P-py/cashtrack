package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.BalanceView
import com.pedrosant.cashtrack.dtos.UserEntry
import com.pedrosant.cashtrack.dtos.UserUpdate
import com.pedrosant.cashtrack.dtos.UserView
import com.pedrosant.cashtrack.exceptions.AccessDeniedException
import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.exceptions.UserAlreadyExistsException
import com.pedrosant.cashtrack.extensions.extractTokenValue
import com.pedrosant.cashtrack.mappers.UserMapper
import com.pedrosant.cashtrack.models.UserCashtrack
import com.pedrosant.cashtrack.repository.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val usersRepository:UserRepository,
    private val mapper:UserMapper,
    private val notFoundMessage:String = "Oh, something went wrong!! User not found!",
    private val tokenService: TokenService
    ) {

    @Cacheable(cacheNames = ["UsersList"], key = "#root.method.name")
    fun getUsers(pageable:Pageable):Page<UserView> {
        try {
            return usersRepository.findAll(pageable)
                .map{ u -> mapper.mapView(u) }
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    @Cacheable(cacheNames = ["UserDetails"], key = "#root.method.name")
    fun getUserById(accessToken:String, userId:Long?):UserView {
        val id = usersRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
        try {
            return mapper.mapView(usersRepository.getReferenceById(userId ?: id))
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    fun exportUserById(id:Long):UserCashtrack {
        return usersRepository.getReferenceById(id)
    }

    @Caching(evict = [CacheEvict("UsersList", allEntries = true), CacheEvict("UserDetails", allEntries = true)])
    fun registerNewUser(newUser:UserEntry):UserView {
        val new = mapper.mapEntry(newUser)
        if(usersRepository.findByEmail(new.email) == null) {
            usersRepository.save(new)
            return mapper.mapView(new)
        } else throw UserAlreadyExistsException("An user for this e-mail already exists.")
    }

    @Cacheable(cacheNames = ["Balance"], key = "#root.method.name")
    fun getBalance(accessToken: String):BalanceView {
        val userId = usersRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
        try {
            val userIncomeList = usersRepository.getReferenceById(userId)
                .incomeList
                .sumOf { i -> i.value }
            val userExpenseList = usersRepository.getReferenceById(userId)
                .expenseList
                .sumOf { i -> i.value }
            return BalanceView(
                totalIncomes = userIncomeList,
                totalExpenses = userExpenseList,
                balance = userIncomeList - userExpenseList
            )
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    @Caching(evict = [CacheEvict("UsersList", allEntries = true), CacheEvict("UserDetails", allEntries = true)])
    fun updateUser(updatedUser:UserUpdate, accessToken: String):UserView {
        val userId = usersRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
        try {
            val update = usersRepository.getReferenceById(userId)
            update.username = updatedUser.username
            update.email = updatedUser.email
            return mapper.mapView(update)
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }

    @Caching(evict = [CacheEvict("UsersList", allEntries = true), CacheEvict("UserDetails", allEntries = true)])
    fun delete(accessToken: String) {
        val id = usersRepository.findByEmail(
            tokenService.extractEmail(accessToken.extractTokenValue())
        )?.id ?: throw AccessDeniedException("You don't have permission to access this page.")
        try {
            val deletedUser = usersRepository.getReferenceById(id)
            usersRepository.delete(deletedUser)
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException("You can't delete a user that does not exist!"))
        }
    }
}