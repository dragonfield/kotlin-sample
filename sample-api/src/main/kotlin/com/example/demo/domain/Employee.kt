package com.example.demo.domain

data class Employee(
    val id: String,
    val firstName: String,
    val lastName: String,
) {
    companion object {
        val NULL = Employee(id = "", firstName = "", lastName = "")
    }
}
