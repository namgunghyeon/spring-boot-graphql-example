package com.graphql.api

import com.graphql.api.config.SecurityProperties
import com.graphql.rds_domain.RdsDomainApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [
	DemoApplication::class,
	RdsDomainApplication::class
])
@EnableConfigurationProperties(SecurityProperties::class)
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
