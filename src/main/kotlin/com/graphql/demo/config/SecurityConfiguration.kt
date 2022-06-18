package com.graphql.demo.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityConfiguration {
    @Bean
    fun jwtAlgorithm(): Algorithm {
        return Algorithm.HMAC256("my-JWT-secret")
    }

    @Bean
    fun verifier(algorithm: Algorithm): JWTVerifier {
        return JWT
            .require(algorithm)
            .withIssuer("my-graphql-api")
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
    }
}