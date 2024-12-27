package io.memorix.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column


object Users : IntIdTable() {
    val name: Column<String> = varchar("name", length = 255)
    val email: Column<String> = varchar("email", length = 255).uniqueIndex()
    val password: Column<String> = varchar("password", length = 1024)
}

class UserDao(id:EntityID<Int>): IntEntity(id) {
    companion object:IntEntityClass<UserDao>(Users)

    var name by Users.name
    var email by Users.email
    var password by Users.password
}