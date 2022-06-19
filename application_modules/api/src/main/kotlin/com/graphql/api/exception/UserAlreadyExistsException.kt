package com.graphql.api.exception

import java.text.MessageFormat

class UserAlreadyExistsException(private val email: String):  RuntimeException() {
    override val message: String
        get() = MessageFormat.format("A user already exists with email ''{0}''", email);
}