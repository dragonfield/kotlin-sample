package com.example.demo.infrastructure.repository

import com.example.demo.application.repository.EmployeeRepository
import com.example.demo.common.SystemException
import com.example.demo.domain.Employee
import com.example.demo.infrastructure.entity.EmployeeEntity
import com.example.demo.infrastructure.mapper.EmployeeMapper
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Repository

@Repository
class EmployeeRepositoryImpl(
    private val mapper: EmployeeMapper
): EmployeeRepository {

    override fun find(id: String): Employee {
        try {
            val entity: EmployeeEntity? = mapper.find(id)
            return entity?.toModel() ?: Employee.NULL
        } catch (e: DataAccessException) {
            throw SystemException(e)
        }
    }

}