package io.memorix

import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.memorix.dao.UserDao
import io.memorix.plugins.*
import io.memorix.dependencies.repositoryModule
import io.memorix.dependencies.serviceModule
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.environmentProperties
import org.koin.ktor.ext.get

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    startKoin()
    configureHTTP()
    configureSerialization()
    configureRouting()
    // init the database connection
    get<Database>()
}

fun startKoin(): Koin = startKoin {
    properties(
        dotenv {
            ignoreIfMalformed = true
            ignoreIfMissing = true
        }
            .entries()
            .associate {
                it.key to it.value
            }
    )

    environmentProperties()

    modules(
        applicationModule,
        repositoryModule,
        serviceModule
    )
}.koin
