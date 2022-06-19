package com.graphql.api.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtUserDetails(
    private val username: String,
    private val password: String,
    private val roles: MutableSet<SimpleGrantedAuthority>,
    private val token: String
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles;
    }

    override fun getPassword(): String {
        return password;
    }

    override fun getUsername(): String {
        return username;
    }

    override fun isAccountNonExpired(): Boolean {
        return true;
    }

    override fun isAccountNonLocked(): Boolean {
        return true;
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true;
    }

    override fun isEnabled(): Boolean {
       return true;
    }
}