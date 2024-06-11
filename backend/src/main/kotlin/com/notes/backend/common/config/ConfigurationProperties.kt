package com.notes.backend.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "notes")
data class ConfigurationProperties(
    val allowedOrigin: String = ""
)
