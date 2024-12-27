package io.memorix.unit

import io.memorix.dependencies.serviceModule
import io.memorix.dto.User
import io.memorix.dto.response.SearchUserResponse
import io.memorix.dto.response.UserResponse
import io.memorix.repositories.UserRepository
import io.memorix.services.UserService
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.assertEquals

val repositoryModule = module {
    single<UserRepository> {
        object : UserRepository {
            private var database = ArrayList<User>()
            override fun createUser(user: User) {
                database.add(user)
            }

            override fun findUserByEmail(email: String): User? {
                return database.find { it.email == email }
            }

            override fun searchUsers(search: String, limit: Long): SearchUserResponse {
                val count = database.size.toLong()
                val filter = database.filter { it.email.startsWith(search) }.map { UserResponse(it.email, it.name) }
                return SearchUserResponse(filter.take(limit.toInt()), count)
            }

        }
    }
}

class UserServiceTest:KoinTest {

    val userService by inject<UserService>()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(serviceModule, repositoryModule)
    }

    @Test
    fun testCreateUser() {
        val user = User("test", "test@agmail.com", "testing123")
        userService.createUser(user)
        val createdUser = userService.searchUsers(user.email, 1)
        assertEquals(user.email, createdUser.users[0].email)
    }

    @Test
    fun testSearchUser() {
        val search = "test"
        val firstCount = 5
        for (i in 0 until firstCount) {
             userService.createUser(User("test$i", "test$i@agmail.com", "testing123"))
        }
        val secondCount = 5;
        for (i in 0 until secondCount) {
            userService.createUser(User("test$i", "not$i@agmail.com", "testing123"))
        }
        val searchUsers = userService.searchUsers(search, 5)
        assertEquals(searchUsers.total, (firstCount + secondCount).toLong())
        assert(searchUsers.users[0].email.startsWith(search))
    }
}