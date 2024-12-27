package io.memorix.dependencies

import io.memorix.repositories.UserRepository
import io.memorix.repositories.implementation.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> {
        UserRepositoryImpl()
    }
}
