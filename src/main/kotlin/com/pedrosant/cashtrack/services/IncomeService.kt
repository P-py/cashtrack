package com.pedrosant.cashtrack.services

import com.pedrosant.cashtrack.dtos.IncomeEntry
import com.pedrosant.cashtrack.dtos.IncomeUpdate
import com.pedrosant.cashtrack.dtos.IncomeView
import com.pedrosant.cashtrack.exceptions.NotFoundException
import com.pedrosant.cashtrack.mappers.IncomeMapper
import com.pedrosant.cashtrack.repository.IncomeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Service

@Service
class IncomeService(
    private var incomesRepository:IncomeRepository,
    private val mapper:IncomeMapper,
    private val userService:UserService,
    private val notFoundMessage:String = "Oh, something went wrong! Income not found!"
    ){
    fun getIncomes(pageable:Pageable):Page<IncomeView>{
//        return incomes.stream().map { i -> mapper.mapView(i) }.toList()
        try {
            // different from the .findAll() default method, using the pageable argument from type
            // spring.data.domain.Pageable it returns a Page interface
            return incomesRepository.findAll(pageable)
                .map { i -> mapper.mapView(i) }
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }
    fun getIncomeById(id:Long):IncomeView{
//        return mapper.mapView(incomes.stream().filter { i -> i.id == id }
//            .findFirst().orElseThrow{ NotFoundException(notFoundMessage) })
        try {
            return mapper.mapView(incomesRepository.getReferenceById(id))
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }
    fun getIncomesByUser(userId:Long, label:String?):List<IncomeView>{
        try {
            userService.getUserById(userId)
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException("There is no user for this id!"))
        }
//        try {
//            return incomes.stream()
//                .filter{ i -> i.user.id == userId }
//                .toList().map { i ->
//                    mapper.mapView(i)
//                }
//        } catch (e:NotFoundException){
//            throw(NotFoundException(notFoundMessage))
//        }
        val incomesList = if (label.isNullOrBlank()){
            incomesRepository.findAll()
        } else {
            incomesRepository.findByincomeLabel(label)
        }
        return incomesList
            .filter { i -> i.userCashtrack.id == userId }
            .toList()
            .map { i -> mapper.mapView(i) }
    }
    fun register(newIncome:IncomeEntry):IncomeView{
        val new = mapper.mapEntry(newIncome)
//        new.id = (incomes.size+1).toLong()
//        incomes = incomes.plus(new)
//        userService.addIncome(new, new.user.id)
        incomesRepository.save(new)
        return mapper.mapView(new)
    }
    fun update(updatedIncome:IncomeUpdate):IncomeView{
//        val current = incomes.stream().filter { i -> i.id == updatedIncome.id }
//            .findFirst()
//            .orElseThrow{ NotFoundException(notFoundMessage) }
//        val update = Income(
//            id = current.id,
//            incomeLabel = updatedIncome.incomeLabel,
//            value = updatedIncome.value,
//            type = updatedIncome.type,
//            user = userService.exportUserById(current.user.id)
//        )
//        incomes = incomes.minus(current).plus(update)
//        userService.updatedIncome(update, current)
        try {
            val update = incomesRepository.getReferenceById(updatedIncome.id)
            update.incomeLabel = updatedIncome.incomeLabel
            update.value = updatedIncome.value
            update.type = updatedIncome.type
            return mapper.mapView(update)
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException(notFoundMessage))
        }
    }
    fun delete(id:Long){
//        val deletedIncome = incomes.stream().filter { i -> i.id == id }
//            .findFirst()
//            .orElseThrow{ NotFoundException(notFoundMessage) }
//        incomes = incomes.minus(deletedIncome)
//        userService.deleteIncome(deletedIncome)
        try {
            val deletedIncome = incomesRepository.getReferenceById(id)
            incomesRepository.delete(deletedIncome)
        } catch (e:JpaObjectRetrievalFailureException){
            throw(NotFoundException("You can't delete a user that does not exist!"))
        }
    }
}