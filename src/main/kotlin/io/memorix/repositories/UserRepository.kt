package io.memorix.repositories

import io.memorix.dto.User
import io.memorix.dto.response.SearchUserResponse

interface UserRepository {
    fun createUser(user: User)
    fun findUserByEmail(email: String): User?
    fun searchUsers(search: String, limit: Long): SearchUserResponse
}
