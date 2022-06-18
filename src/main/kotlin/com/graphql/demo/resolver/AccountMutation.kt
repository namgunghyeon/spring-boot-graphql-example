package com.graphql.demo.resolver

import com.graphql.demo.account.dto.AccountDto
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountMutation {
    @MutationMapping
    fun createAccount(): AccountDto {
        return AccountDto(1, "name", "token")
    }

    @MutationMapping
    fun login(
        @Argument("email") email: String,
        @Argument("password") password: String
    ): AccountDto {
        return AccountDto(1, "name", "token")
    }

    @QueryMapping
    fun me(): AccountDto {
        return AccountDto(1, "name", "token")
    }
}