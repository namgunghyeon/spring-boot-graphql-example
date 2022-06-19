package com.graphql.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "security")
data class SecurityProperties(
    val passwordStrength: Int?,
    val tokenSecret: String?,
    val tokenIssuer: String? = "test",
    val tokenExpiration: Duration? = Duration.ofHours(24)
) {
}