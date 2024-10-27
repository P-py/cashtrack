package com.pedrosant.cashtrack.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("com.cashtrack.cors")
data class CorsConfigurationProperties(
    val allowedOriginPatterns: String,
)