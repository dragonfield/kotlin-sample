package com.example.demo.application.service

import com.example.demo.application.repository.EmployeeRepository
import com.example.demo.common.EmployeeNotFoundException
import com.example.demo.domain.Employee
import org.springframework.stereotype.Service

@Service
class FindEmployeeUseCase(
    private val employeeRepository: EmployeeRepository
) {

    fun findEmployee(id: String): Employee {
        val employee = employeeRepository.find(id)

        if (Employee.NULL.equals(employee)) {
            throw EmployeeNotFoundException(id)
        }

        return employee
    }

}