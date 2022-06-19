package com.graphql.api.account

import com.graphql.api.DemoApplication
import com.graphql.api.account.service.AccountService
import com.graphql.api.resolver.AccountMutation
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.graphql.test.tester.GraphQlTester
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post


@AutoConfigureMockMvc
@SpringBootTest
//@GraphQlTest(controllers = [DemoApplication::class])
class AccountTests @Autowired constructor(
    private val mockMvc: MockMvc
    //private val graphQlTester: GraphQlTester
) {
    @Test
    fun test() {
        val resString = mockMvc.post("/graphql") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"query\":\"mutation {\\n    createAccount(email: \\\"emai1l\\\", password: \\\"password\\\") {\\n        name,\\n        id,\\n        token\\n    }\\n}\",\"variables\":{}}"
        }.asyncDispatch().andDo { print() }.andExpect { status { isOk() } }.andReturn().response.contentAsString
        println(resString)
        //val client = WebTestClient.bindToApplicationContext(context)
        //    .configureClient()
        //    .baseUrl("/graphql")
        //    .build()

        //val tester = HttpGraphQlTester.create(client)

        //val client = WebTestClient.bindToServer()
        //    .baseUrl("http://localhost:8080/graphql")
        //    .build()

        //val tester = HttpGraphQlTester.create(client)
        /*
        val createAccount: String = "mutation {\n" +
                "    createAccount(email: \"email\", password: \"password\") {\n" +
                "        name,\n" +
                "        id,\n" +
                "        token\n" +
                "    }\n" +
                "}"

        val res = graphQlTester
            .document(createAccount)
            .execute()
            //.path("createAccount")
            //.hasValue()
        */
    }
}