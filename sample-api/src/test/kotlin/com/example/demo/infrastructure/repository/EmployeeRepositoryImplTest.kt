package com.example.demo.infrastructure.repository

import org.assertj.core.api.Assertions.assertThat

import com.example.demo.application.repository.EmployeeRepository
import com.example.demo.domain.Employee
import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.configuration.Orthography
import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.junit5.api.DBRider
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.sql.DriverManager
import java.util.concurrent.TimeUnit

@SpringBootTest
@Testcontainers
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, cacheConnection = false)
class EmployeeRepositoryImplTest(
    @Autowired var target: EmployeeRepository
) {

    companion object {
        @Container
        @JvmStatic
        val postgresqlContainer = PostgreSQLContainer(DockerImageName.parse(PostgreSQLContainer.IMAGE).withTag("15.3"))
            .withUsername("user")
            .withPassword("pass")
            .withDatabaseName("sample")

        @JvmStatic
        val connectionHolder: ConnectionHolder = ConnectionHolder {
            DriverManager.getConnection(
                postgresqlContainer.jdbcUrl,
                postgresqlContainer.username,
                postgresqlContainer.password
            )
        }

        @DynamicPropertySource
        @JvmStatic
        fun setup(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
        }

        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            TimeUnit.MILLISECONDS.sleep(1000)
            Flyway.configure().dataSource(
                postgresqlContainer.jdbcUrl,
                postgresqlContainer.username,
                postgresqlContainer.password
            ).load().migrate();
        }

        @AfterAll
        @JvmStatic
        fun tearDownAll() {
            postgresqlContainer.close()
        }

    }

    @Test
    @DataSet("datasets/employee/setup/employees.yml")
    fun `正常に指定したIDでエンティティが取得できる場合`() {
        // setup

        // execute
        val actual = target.find("101")

        // assert
        val expected = Employee(id = "101", firstName="Taro", lastName="Yamada")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @DataSet("datasets/employee/setup/employees.yml")
    fun `指定したIDで従業員が取得できない場合`() {
        // setup

        // execute
        val actual = target.find("9999")

        // assert
        val expected = Employee.NULL
        assertThat(actual).isEqualTo(expected)
    }

}