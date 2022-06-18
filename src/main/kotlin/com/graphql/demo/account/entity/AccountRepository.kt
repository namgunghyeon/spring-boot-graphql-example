package com.graphql.demo.account.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AccountRepository: JpaRepository<Account, Long> {
    fun findByEmail(email: String): Optional<Account>;
}