package com.graphql.api.resolver

import com.graphql.api.account.service.AccountService
import com.graphql.demo.account.dto.AccountDto
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController


@RestController
class AccountMutation(
    private val accountService: AccountService,
    private val authenticationProvider: AuthenticationProvider
    ) {
    @MutationMapping
    fun createAccount(
        @Argument("email") email: String,
        @Argument("password") password: String
    ): AccountDto {
        val account = accountService.createAccount(email, password)
        println(account.id)
        println(account.name)
        println(account.email)
        return AccountDto(account.id!!, account.name, account.email)
    }

    @MutationMapping
    fun login(
        @Argument("email") email: String,
        @Argument("password") password: String
    ): String {
        return try {
            val credentials = UsernamePasswordAuthenticationToken(email, password)
            SecurityContextHolder.getContext().authentication =
                authenticationProvider.authenticate(credentials)

            val account = accountService.getCurrentUser()
            accountService.getToken(account)
        } catch (ex: AuthenticationException) {
            println("error")
            throw BadCredentialsException(email)
        }
    }
}