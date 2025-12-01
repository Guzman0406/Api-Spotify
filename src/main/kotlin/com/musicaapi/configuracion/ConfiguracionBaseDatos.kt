package com.musicaapi.configuracion

import com.musicaapi.infraestructura.base_de_datos.GestorBaseDatos
import io.ktor.server.application.Application

fun Application.configurarBaseDatos() {
    val dbUrl = environment.config.propertyOrNull("database.url")?.getString()
        ?: System.getenv("DATABASE_URL")
        ?: "jdbc:postgresql://localhost:5432/spotify"

    val dbUser = environment.config.propertyOrNull("database.user")?.getString()
        ?: System.getenv("DATABASE_USER")
        ?: "Guzmán"

    val dbPassword = environment.config.propertyOrNull("database.password")?.getString()
        ?: System.getenv("DATABASE_PASSWORD")
        ?: "password"

    GestorBaseDatos.inicializar(
        url = dbUrl,
        usuario = dbUser,
        contraseña = dbPassword
    )
}