package com.musicaapi.presentacion.rutas

import com.musicaapi.aplicacion.servicios.ServicioArtistas
import com.musicaapi.presentacion.dto.*
import com.musicaapi.infraestructura.excepciones.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configurarRutasArtistas(servicioArtistas: ServicioArtistas) {

    route("/artistas") {

        // POST /artistas - Crear artista
        post {
            try {
                val request = call.receive<CrearArtistaRequest>()
                val artista = servicioArtistas.crearArtista(request.name, request.genre)
                val artistaDTO = ArtistaDTO.desdeDominio(artista)
                call.respond(HttpStatusCode.Created, artistaDTO)
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error interno del servidor"))
            }
        }

        // GET /artistas - Obtener todos los artistas
        get {
            try {
                val artistas = servicioArtistas.obtenerTodosLosArtistas()
                val artistasDTO = artistas.map { ArtistaDTO.desdeDominio(it) }
                call.respond(HttpStatusCode.OK, artistasDTO)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener artistas"))
            }
        }

        // GET /artistas/{id} - Obtener artista por ID
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de artista requerido")
            )

            try {
                val artista = servicioArtistas.obtenerArtistaPorId(id)
                val artistaDTO = ArtistaDTO.desdeDominio(artista)
                call.respond(HttpStatusCode.OK, artistaDTO)
            } catch (e: ArtistaNoEncontradoException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener artista"))
            }
        }

        // PUT /artistas/{id} - Actualizar artista
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de artista requerido")
            )

            try {
                val request = call.receive<ActualizarArtistaRequest>()
                val artista = servicioArtistas.actualizarArtista(id, request.name, request.genre)
                val artistaDTO = ArtistaDTO.desdeDominio(artista)
                call.respond(HttpStatusCode.OK, artistaDTO)
            } catch (e: ArtistaNoEncontradoException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al actualizar artista"))
            }
        }

        // DELETE /artistas/{id} - Eliminar artista
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de artista requerido")
            )

            try {
                val eliminado = servicioArtistas.eliminarArtista(id)
                if (eliminado) {
                    call.respond(HttpStatusCode.OK, mapOf("mensaje" to "Artista eliminado correctamente"))
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Artista no encontrado"))
                }
            } catch (e: ArtistaNoEncontradoException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: ViolacionIntegridadException) {
                call.respond(HttpStatusCode.Conflict, mapOf("error" to e.message))
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al eliminar artista"))
            }
        }
    }
}