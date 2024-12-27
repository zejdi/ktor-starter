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
        // check if we have an existing user with the specified email
        val userWithEmail = userRepository.findUserByEmail(user.email)
        // if we have an existing user do not continue
        if (userWithEmail != null) {
            throw BadRequestException("Duplicate e-mail: ${user.email}")
        }
        // create the new user
        userRepository.createUser(
            user.copy(password = passwordService.encrypt(user.password))
        )
    }

    override fun searchUsers(search: String, limit: Long): SearchUserResponse {
        // retrieve the users from the repository
        return userRepository.searchUsers(search, limit)
    }

}