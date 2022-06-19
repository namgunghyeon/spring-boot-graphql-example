package com.graphql.api.resolver

import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

@RestController
class BookMutation {

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    fun newBook() {
        println("dfdfdf")
    }
}