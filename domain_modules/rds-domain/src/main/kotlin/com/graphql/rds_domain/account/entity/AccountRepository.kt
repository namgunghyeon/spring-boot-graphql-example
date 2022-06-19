package com.graphql.rds_domain.entity

import com.graphql.rds_domain.account.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AccountRepository: JpaRepository<Account, Long> {
    fun findByEmail(email: String): Optional<Account>
    fun existsByEmail(email: String): Boolean
}