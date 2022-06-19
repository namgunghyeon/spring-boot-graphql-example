package com.graphql.api.config

import com.graphql.api.account.service.AccountService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import java.util.function.Predicate.not
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Service
class JwtFilter(private val accountService: AccountService): OncePerRequestFilter() {
    private val AUTHORIZATION_HEADER = "Authorization"
    private val BEARER_PATTERN: Pattern = Pattern.compile("^Bearer (.+?)$")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        getToken(request)
            .map(accountService::loadUserByToken)
            .map { userDetails ->
                JwtPreAuthenticationToken(
                    userDetails,
                    WebAuthenticationDetailsSource().buildDetails(request))
            }
            .ifPresent { authentication ->
                SecurityContextHolder.getContext().authentication = authentication
            }

        filterChain.doFilter(request, response);
    }

    private fun getToken(request: HttpServletRequest): Optional<String> {
        return Optional
            .ofNullable(request.getHeader(AUTHORIZATION_HEADER))
            .filter(not(String::isEmpty))
            .map { input: CharSequence? -> BEARER_PATTERN.matcher(input) }
            .filter(Matcher::find)
            .map { matcher -> matcher.group(1) }
    }
}