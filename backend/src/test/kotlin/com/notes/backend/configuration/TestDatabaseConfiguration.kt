package com.notes.backend.configuration

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.simple.JdbcClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import javax.sql.DataSource

@TestConfiguration
class TestDatabaseConfiguration {

    @Bean
    @ServiceConnection
    fun postgresqlContainer(): PostgreSQLContainer<*> =
        PostgreSQLContainer(DockerImageName.parse("postgres:16.3"))
            .withUsername("notes")
            .withPassword("app")
            .withDatabaseName("notes")

    @Bean
    fun jdbcClient(dataSource: DataSource) = JdbcClient.create(dataSource)
}