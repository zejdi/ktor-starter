package io.memorix.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class SearchUserResponse(
    val users: List<UserResponse>,
    val total: Long
)