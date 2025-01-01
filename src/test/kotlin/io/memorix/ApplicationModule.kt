package io.memorix

import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val applicationModule = module {
    single<Database> {
        Database.connect(
            url = "jdbc:h2:mem:postgres;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;"
        )
    }
}