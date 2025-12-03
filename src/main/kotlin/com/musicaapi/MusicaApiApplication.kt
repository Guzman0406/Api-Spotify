package com.musicaapi

import com.musicaapi.aplicacion.servicios.ServicioAlbumes
import com.musicaapi.aplicacion.servicios.ServicioArtistas
import com.musicaapi.aplicacion.servicios.ServicioCanciones
import com.musicaapi.configuracion.configurarBaseDatos
import com.musicaapi.configuracion.configurarCors
import com.musicaapi.configuracion.configurarSerializacion
import com.musicaapi.dominio.repositorios.AlbumRepositorio
import com.musicaapi.dominio.repositorios.ArtistaRepositorio
import com.musicaapi.dominio.repositorios.CancionRepositorio
import com.musicaapi.infraestructura.repositorios.AlbumRepositorioImpl
import com.musicaapi.infraestructura.repositorios.ArtistaRepositorioImpl
import com.musicaapi.infraestructura.repositorios.CancionRepositorioImpl
import com.musicaapi.presentacion.rutas.configurarRutasAlbumes
import com.musicaapi.presentacion.rutas.configurarRutasArtistas
import com.musicaapi.presentacion.rutas.configurarRutasCanciones
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    println(" Iniciando API de Música ")

    configurarSerializacion()
    configurarCors()
    configurarBaseDatos()

    val artistaRepositorio: ArtistaRepositorio = ArtistaRepositorioImpl()
    val albumRepositorio: AlbumRepositorio = AlbumRepositorioImpl()
    val cancionRepositorio: CancionRepositorio = CancionRepositorioImpl()

    val servicioArtistas = ServicioArtistas(artistaRepositorio)
    val servicioAlbumes = ServicioAlbumes(albumRepositorio, artistaRepositorio)
    val servicioCanciones = ServicioCanciones(cancionRepositorio, albumRepositorio)

    routing {

        get("/") {
            call.respond(HttpStatusCode.OK, mapOf(
                "mensaje" to "🎵 API de Música funcionando correctamente",
                "version" to "1.0.0",
                "endpoints" to mapOf(
                    "artistas" to "/artistas",
                    "albumes" to "/albumes",
                    "canciones" to "/canciones"
                )
            ))
        }

        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf(
                "estado" to "saludable",
                "timestamp" to System.currentTimeMillis()
            ))
        }

        configurarRutasArtistas(servicioArtistas, servicioAlbumes)
        configurarRutasAlbumes(servicioAlbumes)
        configurarRutasCanciones(servicioCanciones)
    }

    println(" Servidor iniciado en http://0.0.0.0:8080")
    println(" Documentación de endpoints:")
    println("   GET  /              - Estado de la API")
    println("   GET  /health        - Salud del sistema")
    println("   GET  /artistas      - Listar artistas")
    println("   POST /artistas      - Crear artista")
    println("   GET  /artistas/{id} - Obtener artista")
    println("   PUT  /artistas/{id} - Actualizar artista")
    println("   DELETE /artistas/{id} - Eliminar artista")
    println("   GET  /albumes       - Listar álbumes")
    println("   POST /albumes       - Crear álbum")
    println("   GET  /albumes/{id}  - Obtener álbum")
    println("   PUT  /albumes/{id}  - Actualizar álbum")
    println("   DELETE /albumes/{id} - Eliminar álbum")
    println("   GET  /artistas/{id}/albumes - Álbumes por artista")
    println("   GET  /canciones     - Listar canciones")
    println("   POST /canciones     - Crear canción")
    println("   GET  /canciones/{id} - Obtener canción")
    println("   PUT  /canciones/{id} - Actualizar canción")
    println("   DELETE /canciones/{id} - Eliminar canción")
    println("   GET  /albumes/{id}/canciones - Canciones por álbum")
}