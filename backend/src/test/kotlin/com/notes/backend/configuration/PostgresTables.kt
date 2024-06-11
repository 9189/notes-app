package com.notes.backend.configuration

import org.springframework.jdbc.core.simple.JdbcClient

object PostgresTables {

    fun truncateAll(jdbcClient: JdbcClient) = getTableNames(jdbcClient)
        .forEach { jdbcClient.sql("TRUNCATE TABLE $it CASCADE").update() }

    private fun getTableNames(jdbcClient: JdbcClient) = jdbcClient.sql(
        """
            SELECT table_name
            FROM information_schema.tables
            WHERE table_schema = current_schema();
        """.trimIndent()
    ).query(String::class.java).list()

}