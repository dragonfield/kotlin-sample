package com.example.demo

import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.configuration.Orthography
import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.junit5.api.DBRider
import io.restassured.RestAssured
import io.restassured.RestAssured.*
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.sql.DriverManager

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, cacheConnection = false)
class EmployeeIT {

    companion object {
        const val JDBC_URL = "jdbc:postgresql://localhost:5432/mydb"
        const val JDBC_USERNAME = "appuser"
        const val JDBC_PASSWORD = "password123"

        @JvmStatic
        val connectionHolder: ConnectionHolder = ConnectionHolder {
            DriverManager.getConnection(
                JDBC_URL,
                JDBC_USERNAME,
                JDBC_PASSWORD
            )
        }

        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            RestAssured.baseURI = "http://localhost:8080"
        }
    }

    @Test
    @DataSet("datasets/employee/setup/employees.yml")
    fun `正常にIDで指定した従業員情報が取得できる場合`() {
        given()
            .`when`()
            .pathParam("id", "101")["/api/v1/employees/{id}"]
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo("101"))
            .body("firstName", equalTo("Taro"))
            .body("lastName", equalTo("Yamada"))
    }

    @Test
    @DataSet("datasets/employee/setup/employees.yml")
    fun `従業員情報が取得できない場合`() {
        given()
            .`when`()
            .pathParam("id", "9999")["/api/v1/employees/{id}"]
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("type", equalTo("about:blank"))
            .body("status", equalTo(400))
            .body("title", equalTo("Bad Request"))
            .body("instance", equalTo("/api/v1/employees/9999"))
            .body("detail", equalTo("specified employee (id=9999) is not found."))
    }

}