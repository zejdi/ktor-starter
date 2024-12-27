package io.memorix.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val email:String,
    val name:String
)
