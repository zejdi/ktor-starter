package io.memorix.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.memorix.controller.user
import io.memorix.dto.response.ErrorResponse

fun Application.configureRouting() {
    install(AutoHeadResponse)
    install(Resources)
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when(cause) {
                is BadRequestException -> {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse(cause.message ?: "Bad Request"))
                }
                else -> {
                    call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
                }
            }
        }
    }
    routing {
        user()
    }
}
