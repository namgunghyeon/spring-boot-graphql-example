package com.graphql.demo.account.service

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.graphql.demo.account.entity.Account
import com.graphql.demo.account.entity.AccountRepository
import com.graphql.demo.config.JwtUserDetails
import com.graphql.demo.exception.BadTokenException
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Predicate.not
import java.util.stream.Stream


@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val jwtVerifier: JWTVerifier
    ): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return accountRepository.findByEmail(username)
            .map { account -> JwtUserDetails(account.name, account.password, mutableSetOf(), "")}
            .orElseThrow();
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


    private fun getUserDetails(account: Account, token: String): JwtUserDetails {
        return JwtUserDetails(
            account.name,
            account.password,
            mutableSetOf(),
            token
        )
    }

    fun <T> collectionStream(collection: Collection<T>?): Stream<T>? {
        return if (collection == null || collection.isEmpty()) Stream.empty() else collection.stream()
    }

    private fun isAnonymous(authentication: Authentication): Boolean {
        return authentication is AnonymousAuthenticationToken
    }

    private fun getDecodedToken(token: String): Optional<DecodedJWT> {
        return try {
            Optional.of(jwtVerifier.verify(token))
        } catch (ex: JWTVerificationException) {
            Optional.empty()
        }
    }
}