package com.pedrosant.cashtrack.exceptions

import com.pedrosant.cashtrack.dtos.ErrorView
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException
import kotlin.io.AccessDeniedException

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(
        exception:NotFoundException,
        request:HttpServletRequest
        ):ErrorView{
        return ErrorView(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.name,
            message = exception.message,
            path = request.servletPath
        )
    }
    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAlreadyExists(
        exception:UserAlreadyExistsException,
        request:HttpServletRequest
    ):ErrorView{
        return ErrorView(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.name,
            message = exception.message,
            path = request.servletPath
        )
    }
    @ExceptionHandler(com.pedrosant.cashtrack.exceptions.AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleDenied(
        exception:com.pedrosant.cashtrack.exceptions.AccessDeniedException,
        request:HttpServletRequest
    ):ErrorView{
        return ErrorView(
            status = HttpStatus.FORBIDDEN.value(),
            error = HttpStatus.FORBIDDEN.name,
            message = exception.message,
            path = request.servletPath
        )
    }
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationError(
        exception:MethodArgumentNotValidException,
        request:HttpServletRequest
    ):ErrorView{
        val errorMessage = HashMap<String, String?>()
        exception.bindingResult.fieldErrors.forEach{
            e -> errorMessage[e.field] = e.defaultMessage
        }
        return ErrorView(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.name,
            message = errorMessage.toString(),
            path = request.servletPath
        )
    }
    @ExceptionHandler(NoResourceFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleResourceNotFound(
        exception:NoResourceFoundException,
        request:HttpServletRequest
    ):ErrorView{
        return ErrorView(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.name,
            message = exception.message,
            path = request.servletPath
        )
    }
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    fun handleGeneric(
        exception:Exception,
        request:HttpServletRequest
    ):ErrorView{
        return ErrorView(
            status = HttpStatus.I_AM_A_TEAPOT.value(),
            error = HttpStatus.I_AM_A_TEAPOT.name,
            message = exception.message,
            path = request.servletPath
        )
    }
}