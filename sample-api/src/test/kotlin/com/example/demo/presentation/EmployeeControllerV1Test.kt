package com.example.demo.presentation

import com.example.demo.application.service.FindEmployeeUseCase
import com.example.demo.common.EmployeeNotFoundException
import com.example.demo.common.SystemException
import com.example.demo.domain.Employee
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.QueryTimeoutException
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest
class EmployeeControllerV1Test {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var findEmployeeUseCase: FindEmployeeUseCase

    @BeforeEach
    fun setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    @Test
    fun `正常にIDで指定した従業員情報が取得できる場合`() {
        // setup
        doReturn(Employee(id = "0001", firstName = "Taro", lastName = "Yamada"))
            .`when`(findEmployeeUseCase).findEmployee("0001")

        // execute & assert
        given()
            .`when`()["/api/v1/employees/0001"]
            .then()
            .status(HttpStatus.OK)
            .body("id", equalTo("0001"))
            .body("firstName", equalTo("Taro"))
            .body("lastName", equalTo("Yamada"))
    }

    @Test
    fun `従業員情報が取得できない場合`() {
        // setup
        doThrow(EmployeeNotFoundException(id = "9999"))
            .`when`(findEmployeeUseCase).findEmployee("9999")

        // execute & assert
        given()
            .`when`()["/api/v1/employees/9999"]
            .then()
            .status(HttpStatus.BAD_REQUEST)
            .body("type", equalTo("about:blank"))
            .body("status", equalTo(400))
            .body("title", equalTo("Bad Request"))
            .body("instance", equalTo("/api/v1/employees/9999"))
            .body("detail", equalTo("specified employee (id=9999) is not found."))
    }

    @Test
    fun `システム障害が発生した場合`() {
        // setup
        doThrow(SystemException(QueryTimeoutException("data access error")))
            .`when`(findEmployeeUseCase).findEmployee("9999")

        // execute & assert
        given()
            .`when`()["/api/v1/employees/9999"]
            .then()
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("type", equalTo("about:blank"))
            .body("status", equalTo(500))
            .body("title", equalTo("Internal Server Error"))
            .body("instance", equalTo("/api/v1/employees/9999"))
            .body("detail", equalTo("org.springframework.dao.QueryTimeoutException: data access error"))
    }


}