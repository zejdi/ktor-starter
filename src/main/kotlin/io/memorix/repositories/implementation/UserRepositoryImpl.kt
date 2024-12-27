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
            // check if there is a user with the specified email
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
        // store the total number of users
        var total: Long = 0
        val users = transaction {
            total = Users.selectAll().count()
            // find the users with email that match the search value
            val users = UserDao.find { Users.email like "$search%" }
            // transform the user dao to dto
            users.map { UserResponse(it.email, it.name) }
        }
        // create the response dto and use limit value to retrieve only
        // the specified number of users
        return SearchUserResponse(users.take(limit.toInt()), total)
    }

}