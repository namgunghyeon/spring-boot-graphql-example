package com.graphql.api.exception

class BadTokenException(): RuntimeException() {
    override val message: String
        get() = "Token is inavlid or expired"
}