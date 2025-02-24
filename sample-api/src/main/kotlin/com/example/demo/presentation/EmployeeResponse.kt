package com.example.demo.presentation

import com.example.demo.domain.Employee
import com.fasterxml.jackson.annotation.JsonProperty

data class EmployeeResponse(
    @field:JsonProperty("id")
    var id: String,
    @field:JsonProperty("firstName")
    var firstName: String,
    @field:JsonProperty("lastName")
    var lastName: String,
) {
    companion object {
        fun of(employee: Employee): EmployeeResponse {
            return EmployeeResponse(
                id = employee.id,
                firstName = employee.firstName,
                lastName = employee.lastName,
            )
        }
    }
}
