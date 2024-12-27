package io.memorix.services

import io.memorix.dto.User
import io.memorix.dto.response.SearchUserResponse

interface UserService {

    fun createUser(user: User)

    fun searchUsers(search: String, limit: Long): SearchUserResponse
}