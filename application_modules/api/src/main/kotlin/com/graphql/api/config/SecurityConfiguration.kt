package com.graphql.api.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.graphql.api.account.service.AccountService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
class SecurityConfiguration(
    private val securityProperties: SecurityProperties
) {
    @Bean
    fun jwtAlgorithm(): Algorithm {
        return Algorithm.HMAC256("my-JWT-secret")
    }

    @Bean
    fun verifier(algorithm: Algorithm): JWTVerifier {
        return JWT
            .require(algorithm)
            .withIssuer(securityProperties.tokenIssuer)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
    }

    @Bean
    fun authenticationProvider(
        accountService: AccountService,
        passwordEncoder: PasswordEncoder?
    ): AuthenticationProvider? {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(accountService)
        provider.setPasswordEncoder(passwordEncoder)
        return provider
    }
}