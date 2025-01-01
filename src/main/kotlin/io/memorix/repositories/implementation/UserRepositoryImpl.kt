package io.memorix.repositories.implementation

import io.memorix.dao.UserDao
import io.memorix.dao.Users
import io.memorix.dto.User
import io.memorix.dto.response.SearchUserResponse
import io.memorix.dto.response.UserResponse
import io.memorix.repositories.UserRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl : UserRepository {

    init {
        transaction {
            SchemaUtils.create(UserDao.table)
        }
    }

    override fun findUserByEmail(email: String): User? {
        return transaction {
            UserDao.find { Users.email eq email }.firstOrNull()?.let {
                User(
                    it.name,
                    it.email,
                    it.password
                )
            }
        }
    }

    override fun createUser(user: User) {
        transaction {
            UserDao.new {
                name = user.name
                email = user.email
                password = user.password
            }
        }
    }

    override fun searchUsers(search: String, limit: Long): SearchUserResponse {
        var totalNumberOfUsers: Long = 0
        val users = transaction {
            totalNumberOfUsers = Users.selectAll().count()
            val users = UserDao.find { Users.email like "$search%" }
            users.map { UserResponse(it.email, it.name) }
        }
        return SearchUserResponse(users.take(limit.toInt()), totalNumberOfUsers)
    }

}