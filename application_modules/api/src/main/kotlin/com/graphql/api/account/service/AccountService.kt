package com.graphql.api.account.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.graphql.api.account.entity.Account
import com.graphql.api.account.entity.AccountRepository
import com.graphql.api.config.JwtUserDetails
import com.graphql.api.config.SecurityConfiguration
import com.graphql.api.config.SecurityProperties
import com.graphql.api.exception.BadTokenException
import com.graphql.api.exception.UserAlreadyExistsException
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import java.util.function.Predicate.not
import java.util.stream.Stream


@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val securityProperties: SecurityProperties,
    private val algorithm: Algorithm,
    private val jwtVerifier: JWTVerifier
    ): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val userDetails = accountRepository
            .findByEmail(username)
            .map { account -> JwtUserDetails(account.email, account.password, mutableSetOf(), "")}
            .orElseThrow();

        return userDetails
    }

    fun createAccount(email: String, password: String): Account {
        if (this.exists(email)) {
            throw UserAlreadyExistsException(email)
        }

        return accountRepository.save(Account(email, email, passwordEncoder.encode(password)))
    }

    fun getToken(account: Account): String {
        val now = Instant.now()
        val expiry = Instant.now().plus(securityProperties.tokenExpiration)
        return JWT
            .create()
            .withIssuer(securityProperties.tokenIssuer)
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(expiry))
            .withSubject(account.email)
            .sign(algorithm)
    }

    @Transactional
    fun loadUserByToken(token: String): JwtUserDetails {
        return getDecodedToken(token)
            .map { obj: DecodedJWT -> obj.subject }
            .flatMap(accountRepository::findByEmail)
            .map { user -> getUserDetails(user, token) }
            .orElseThrow{BadTokenException()}
    }

    fun isAuthenticated(): Boolean {
        return Optional
            .ofNullable(SecurityContextHolder.getContext())
            .map { obj: SecurityContext -> obj.authentication }
            .filter(Authentication::isAuthenticated)
            .filter(not(this::isAnonymous))
            .isPresent
    }

    fun getCurrentUser(): Account {
        return Optional
            .ofNullable(SecurityContextHolder.getContext())
            .map { obj: SecurityContext -> obj.authentication }
            .map { obj: Authentication -> obj.name }
            .flatMap(accountRepository::findByEmail)
            .orElse(null)
    }

    fun <T> collectionStream(collection: Collection<T>?): Stream<T>? {
        return if (collection == null || collection.isEmpty()) Stream.empty() else collection.stream()
    }

    private fun exists(email: String): Boolean {
        return accountRepository.existsByEmail(email)
    }

    private fun getUserDetails(account: Account, token: String): JwtUserDetails {
        return JwtUserDetails(
            account.name,
            account.password,
            mutableSetOf(),
            token
        )
    }

    private fun isAnonymous(authentication: Authentication): Boolean {
        return authentication is AnonymousAuthenticationToken
    }

    private fun getDecodedToken(token: String): Optional<DecodedJWT> {
        return try {
            Optional.of(jwtVerifier.verify(token))
        } catch (ex: JWTVerificationException) {
            println(ex)
            Optional.empty()
        }
    }
}