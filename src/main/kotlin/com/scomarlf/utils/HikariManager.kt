package com.scomarlf.utils

import com.scomarlf.conf.DatabaseConf
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.Connection


class HikariManager {

    companion object {
        var ds: HikariDataSource? = null
        var connection: Connection? = null

        fun createConnection() {
            val config = HikariConfig()
            config.jdbcUrl = DatabaseConf.URL
            config.username = DatabaseConf.USERNAME
            config.password = DatabaseConf.PASSWORD
            config.addDataSourceProperty("cachePrepStmts", "true")
            config.addDataSourceProperty("prepStmtCacheSize", "250")
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
            config.addDataSourceProperty("useServerPrepStmts", "true")
            ds = HikariDataSource(config)
            connection = ds!!.connection;
        }

        fun getDSLContext(): DSLContext {
            return DSL.using(connection, SQLDialect.MYSQL)
        }

    }

}