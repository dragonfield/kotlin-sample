package com.example.demo.common

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(EmployeeNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleEmployeeNotFoundException(e: EmployeeNotFoundException): ProblemDetail {
        val userId = e.id
        val message = "specified employee (id=${userId}) is not found."
        log.info(message, e)
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message)
        problemDetail.status = HttpStatus.BAD_REQUEST.value()
        return problemDetail
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleUnexpectedException(e: Exception): ProblemDetail {
        log.error("unexpected exception is occurred.", e)
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        problemDetail.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        return problemDetail
    }
}
