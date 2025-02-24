package com.example.demo.application.repository

import com.example.demo.domain.Employee

interface EmployeeRepository {
    fun find(id: String): Employee
}
