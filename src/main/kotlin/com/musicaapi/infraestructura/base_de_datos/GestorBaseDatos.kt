package com.musicaapi.infraestructura.base_de_datos

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object GestorBaseDatos {

    fun inicializar(
        url: String,
        usuario: String,
        contraseña: String,
        driver: String = "org.postgresql.Driver"
    ) {
        val config = HikariConfig().apply {
            this.jdbcUrl = url
            this.driverClassName = driver
            this.username = usuario
            this.password = contraseña
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(TablaArtistas, TablaAlbumes, TablaCanciones)
        }

        println("✅ Base de datos inicializada correctamente")
    }

    suspend fun <T> consulta(bloque: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { bloque() }
}