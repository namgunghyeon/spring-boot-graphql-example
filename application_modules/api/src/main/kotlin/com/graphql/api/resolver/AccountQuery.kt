package com.graphql.api.resolver

import com.graphql.api.account.service.AccountService
import com.graphql.demo.account.dto.AccountDto
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountQuery(
    private val accountService: AccountService
) {
    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    fun me(): AccountDto {
        val account = accountService.getCurrentUser()
        return AccountDto(account.id!!, account.name, account.email)
    }
}