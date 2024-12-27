package io.memorix.services

interface PasswordService {
    fun encrypt(password: String): String
    fun verify(password: String, encyrptedPassword: String): Boolean
}