package com.example.demo

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class DemoApplicationKtTest(
    @Autowired val context: ApplicationContext
) {

    @Test
    fun contextLoads() {
        assertThat(context).isNotNull()
    }

}