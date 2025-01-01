package io.memorix.integration

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.memorix.dto.User
import io.memorix.dto.response.ErrorResponse
import io.memorix.dto.response.SearchUserResponse
import io.memorix.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserRouteTest {

    @Test
    fun testCreateUser() = testApplication {
        application {
            module()
        }
        val user = User("test", "test@gmail.com", "testing123")

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/users") {
            setBody(user)
            contentType(ContentType.Application.Json)
        }
        assertEquals(response.status, HttpStatusCode.Accepted)
    }

    @Test
    fun testCreateUserExistingUser() = testApplication {
        application {
            module()
        }
        val user = User("test", "test@gmail.com", "testing123")

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.post("/users") {
            setBody(user)
            contentType(ContentType.Application.Json)
        }
        val response = client.post("/users") {
            setBody(user)
            contentType(ContentType.Application.Json)
        }

        assertEquals(response.status, HttpStatusCode.BadRequest)
    }

    @Test
    fun testSearchUserNoUsers() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("/users") {
            parameter("query", "test")
            parameter("limit", "5")
        }
        val body = response.body<SearchUserResponse>()
        assertEquals(body.users.size, 0)
    }


    @Test
    fun testSearchUserLimit() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        for (index in 1 until 5) {
            val user = User("test$index", "test$index@gmail.com", "testing123")


            client.post("/users") {
                setBody(user)
                contentType(ContentType.Application.Json)
            }
        }
        val response = client.get("/users") {
            parameter("query", "test")
            parameter("limit", "2")
        }
        val body = response.body<SearchUserResponse>()
        assertEquals(body.users.size, 2)
    }

    @Test
    fun testSearchUserQuery() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val query = "test"

        for (index in 1 until 5) {
            val user = User("test$index", "test$index@gmail.com", "testing123")


            client.post("/users") {
                setBody(user)
                contentType(ContentType.Application.Json)
            }
        }
        val response = client.get("/users") {
            parameter("query", query)
            parameter("limit", "2")
        }
        val body = response.body<SearchUserResponse>()
        assertTrue { body.users.all { it.email.startsWith(query) } }
    }

    @Test
    fun testSearchUserQueryEmpty() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val query = "none"

        for (index in 1 until 5) {
            val user = User("test$index", "test$index@gmail.com", "testing123")


            client.post("/users") {
                setBody(user)
                contentType(ContentType.Application.Json)
            }
        }
        val response = client.get("/users") {
            parameter("query", query)
            parameter("limit", "2")
        }
        val body = response.body<SearchUserResponse>()
        assertEquals(body.users.size, 0)
    }

}