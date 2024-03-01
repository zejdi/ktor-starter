package io.memorix.user

import io.vertx.jdbcclient.JDBCPool

class UserRepository(
    val client: JDBCPool
) {
    // Add your repository methods here
}
