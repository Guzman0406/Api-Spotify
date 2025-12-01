package com.musicaapi.presentacion.rutas

import com.musicaapi.aplicacion.servicios.ServicioAlbumes
import com.musicaapi.presentacion.dto.*
import com.musicaapi.infraestructura.excepciones.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configurarRutasAlbumes(servicioAlbumes: ServicioAlbumes) {

    route("/albumes") {

        // POST /albumes - Crear álbum
        post {
            try {
                val request = call.receive<CrearAlbumRequest>()
                val album = servicioAlbumes.crearAlbum(request.title, request.artistaId, request.releaseYear)
                val albumDTO = AlbumDTO.desdeDominio(album)
                call.respond(HttpStatusCode.Created, albumDTO)
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: ArtistaNoEncontradoException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error interno del servidor"))
            }
        }

        // GET /albumes - Obtener todos los álbumes
        get {
            try {
                val albumes = servicioAlbumes.obtenerTodosLosAlbumes()
                val albumesDTO = albumes.map { AlbumDTO.desdeDominio(it) }
                call.respond(HttpStatusCode.OK, albumesDTO)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener álbumes"))
            }
        }

        // GET /albumes/{id} - Obtener álbum por ID
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de álbum requerido")
            )

            try {
                val album = servicioAlbumes.obtenerAlbumPorId(id)
                val albumDTO = AlbumDTO.desdeDominio(album)
                call.respond(HttpStatusCode.OK, albumDTO)
            } catch (e: AlbumNoEncontradoException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener álbum"))
            }
        }

        // PUT /albumes/{id} - Actualizar álbum
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de álbum requerido")
            )

            try {
                val request = call.receive<ActualizarAlbumRequest>()
                val album = servicioAlbumes.actualizarAlbum(id, request.title, request.releaseYear)
                val albumDTO = AlbumDTO.desdeDominio(album)
                call.respond(HttpStatusCode.OK, albumDTO)
            } catch (e: AlbumNoEncontradoException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al actualizar álbum"))
            }
        }

        // DELETE /albumes/{id} - Eliminar álbum
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de álbum requerido")
            )

            try {
                val eliminado = servicioAlbumes.eliminarAlbum(id)
                if (eliminado) {
                    call.respond(HttpStatusCode.OK, mapOf("mensaje" to "Álbum eliminado correctamente"))
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Álbum no encontrado"))
                }
            } catch (e: AlbumNoEncontradoException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: ViolacionIntegridadException) {
                call.respond(HttpStatusCode.Conflict, mapOf("error" to e.message))
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al eliminar álbum"))
            }
        }

        // GET /artistas/{artistaId}/albumes - Obtener álbumes por artista
        get("/artistas/{artistaId}/albumes") {
            val artistaId = call.parameters["artistaId"] ?: return@get call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de artista requerido")
            )

            try {
                val albumes = servicioAlbumes.obtenerAlbumesPorArtista(artistaId)
                val albumesDTO = albumes.map { AlbumDTO.desdeDominio(it) }
                call.respond(HttpStatusCode.OK, albumesDTO)
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener álbumes del artista"))
            }
        }
    }
}