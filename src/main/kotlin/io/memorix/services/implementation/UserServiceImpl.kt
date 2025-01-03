package io.memorix.services.implementation

import io.ktor.server.plugins.*
import io.memorix.dto.User
import io.memorix.dto.response.SearchUserResponse
import io.memorix.repositories.UserRepository
import io.memorix.services.PasswordService
import io.memorix.services.UserService

class UserServiceImpl(
    val userRepository: UserRepository,
    val passwordService: PasswordService
) : UserService {

    override fun createUser(user: User) {
        val existingUser = userRepository.findUserByEmail(user.email)
        if (existingUser != null) {
            throw BadRequestException("Duplicate e-mail: ${user.email}")
        }
        userRepository.createUser(
            user.copy(password = passwordService.encrypt(user.password))
        )
    }

    override fun searchUsers(search: String, limit: Long): SearchUserResponse {
        return userRepository.searchUsers(search, limit)
    }

}