package io.memorix.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.memorix.dto.User
import io.memorix.services.UserService
import org.koin.ktor.ext.inject

fun Route.user() {
    val userService: UserService by inject()
    get("/users") {
        val queryParameters = call.request.queryParameters
        val search = queryParameters["query"]
        val limit = queryParameters["limit"]?.toLongOrNull()
        if (search.isNullOrEmpty() || limit == null) {
            throw BadRequestException("Invalid query params provided")
        }
        call.respond(HttpStatusCode.OK, userService.searchUsers(search, limit))
    }

    post("/users") {
        val user = call.receive<User>()
        userService.createUser(user)
        call.respond(HttpStatusCode.Accepted)
    }
}
