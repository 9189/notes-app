package com.notes.backend.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClient.builder

@TestConfiguration
class TestWebConfiguration {

    @Bean
    @Lazy
    fun restClient(
        @Value("\${local.server.port}") port: Int,
    ): RestClient = builder().baseUrl("http://localhost:$port").build()
}