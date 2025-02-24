package com.example.demo.application.service

import com.example.demo.application.repository.EmployeeRepository
import com.example.demo.common.EmployeeNotFoundException
import com.example.demo.domain.Employee
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.MockitoAnnotations

class FindEmployeeUseCaseTest {
    @InjectMocks
    lateinit var target: FindEmployeeUseCase

    @Mock
    lateinit var employeeRepository: EmployeeRepository

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `正常に指定したIDで従業員のモデルが取得できる場合`() {
        // setup
        doReturn(Employee(id = "001", firstName = "Taro", lastName = "Yamada"))
            .`when`(employeeRepository)
            .find("001")

        // execute
        val actual = target.findEmployee("001")

        // assert
        val expected = Employee(id = "001", firstName = "Taro", lastName = "Yamada")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `指定したIDで従業員のモデルが取得できない場合`() {
        // setup
        doThrow(EmployeeNotFoundException(id = "9999"))
            .`when`(employeeRepository)
            .find("9999")

        // execute & assert
        assertThatThrownBy {
            target.findEmployee("9999")
        }.isInstanceOfSatisfying(EmployeeNotFoundException::class.java, {
            assertThat(it.id).isEqualTo("9999")
        })
    }
}
