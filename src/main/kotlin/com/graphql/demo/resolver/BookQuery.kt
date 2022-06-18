package com.graphql.demo.resolver

import com.graphql.demo.account.entity.Book
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BookQuery {
    @QueryMapping
    fun allBook(): List<Book> {
        return listOf(Book(1))
    }
}