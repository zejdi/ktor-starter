package io.memorix.dependencies

import io.memorix.services.PasswordService
import io.memorix.services.UserService
import io.memorix.services.implementation.PasswordServiceImpl
import io.memorix.services.implementation.UserServiceImpl
import org.koin.dsl.module

val serviceModule = module {
    single<PasswordService> {
        PasswordServiceImpl()
    }
    single<UserService> {
        UserServiceImpl(
            userRepository = get(),
            passwordService = get()
        )
    }
}