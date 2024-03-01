package io.memorix

import io.vertx.core.Vertx
import io.vertx.jdbcclient.JDBCConnectOptions
import io.vertx.jdbcclient.JDBCPool
import io.vertx.sqlclient.PoolOptions
import org.koin.dsl.module

val applicationDi = module {
    single<JDBCPool> {
        val url = "jdbc:postgresql://" + getProperty("DB_HOST") + ":" + getProperty("DB_PORT") + "/" + getProperty("DB_NAME") + "?sslmode=disable"
        JDBCPool.pool(
            Vertx.vertx(),
            JDBCConnectOptions()
                .setJdbcUrl(url)
                .setUser(getProperty("DB_USER"))
                .setPassword(getProperty("DB_PASS")),
            PoolOptions()
                .setMaxSize(6)
                .setName("pg-pool")
        )
    }
}
