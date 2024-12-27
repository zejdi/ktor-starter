package io.memorix.services.implementation

import com.password4j.Password
import io.memorix.services.PasswordService

class PasswordServiceImpl : PasswordService {
    override fun encrypt(password:String):String {
        try {
            // hash the password
            return Password.hash(password.toByteArray()).withArgon2().toString()
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun verify(password: String, encyrptedPassword:String): Boolean {
        return Password.check(password.toByteArray(), encyrptedPassword.toByteArray()).withArgon2()
    }
}