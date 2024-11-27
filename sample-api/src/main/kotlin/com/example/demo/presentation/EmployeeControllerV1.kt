package com.example.demo.presentation

import com.example.demo.application.service.FindEmployeeUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/employees")
class EmployeeControllerV1(
    private val findEmployeeUseCase: FindEmployeeUseCase
) {

    @GetMapping("/{id}")
    fun getEmployee(@PathVariable id: String): EmployeeResponse {
        return EmployeeResponse.of(findEmployeeUseCase.findEmployee(id))
    }

}