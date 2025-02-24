package com.example.demo.common

class EmployeeNotFoundException(
    val id: String,
) : RuntimeException()
