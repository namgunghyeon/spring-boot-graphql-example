package com.graphql.demo.config

import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

class JwtPreAuthenticationToken(
    private val jwtUserDetails: JwtUserDetails,
    private val webAuthenticationDetails: WebAuthenticationDetails
    ): PreAuthenticatedAuthenticationToken(jwtUserDetails, null, jwtUserDetails.authorities) {

    fun JwtPreAuthenticationToken(principal: JwtUserDetails, details: WebAuthenticationDetails?) {
        super.setDetails(details)
    }

    override fun getCredentials(): Any? {
        return null;
    }
}