package com.graphql.demo.exception

class BadTokenException(): RuntimeException() {
    override val message: String
        get() = "Token is inavlid or expired"
}