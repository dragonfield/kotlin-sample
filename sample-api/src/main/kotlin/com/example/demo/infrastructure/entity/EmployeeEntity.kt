package com.example.demo.infrastructure.entity

import com.example.demo.domain.Employee

data class EmployeeEntity(
    var id: String,
    var firstName: String,
    var lastName: String,
) {
    fun toModel(): Employee {
        return Employee(id = this.id, firstName = this.firstName, lastName = this.lastName)
    }
}
