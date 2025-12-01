package com.musicaapi.presentacion.rutas

import com.musicaapi.aplicacion.servicios.ServicioCanciones
import com.musicaapi.presentacion.dto.*
import com.musicaapi.infraestructura.excepciones.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configurarRutasCanciones(servicioCanciones: ServicioCanciones) {

    route("/canciones") {

        // POST /canciones - Crear canción
        post {
            try {
                val request = call.receive<CrearCancionRequest>()
                val cancion = servicioCanciones.crearCancion(request.titulo, request.albumId, request.duracion)
                val cancionDTO = CancionDTO.desdeDominio(cancion)
                call.respond(HttpStatusCode.Created, cancionDTO)
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: AlbumNoEncontradoException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error interno del servidor: ${e.localizedMessage}"))
            }
        }

        // GET /canciones - Obtener todas las canciones
        get {
            try {
                val canciones = servicioCanciones.obtenerTodasLasCanciones()
                val cancionesDTO = canciones.map { CancionDTO.desdeDominio(it) }
                call.respond(HttpStatusCode.OK, cancionesDTO)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener canciones: ${e.localizedMessage}"))
            }
        }

        // GET /canciones/{id} - Obtener canción por ID
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de canción requerido")
            )

            try {
                val cancion = servicioCanciones.obtenerCancionPorId(id)
                val cancionDTO = CancionDTO.desdeDominio(cancion)
                call.respond(HttpStatusCode.OK, cancionDTO)
            } catch (e: CancionNoEncontradaException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener canción: ${e.localizedMessage}"))
            }
        }

        // PUT /canciones/{id} - Actualizar canción
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de canción requerido")
            )

            try {
                val request = call.receive<ActualizarCancionRequest>()
                val cancion = servicioCanciones.actualizarCancion(id, request.titulo, request.duracion)
                val cancionDTO = CancionDTO.desdeDominio(cancion)
                call.respond(HttpStatusCode.OK, cancionDTO)
            } catch (e: CancionNoEncontradaException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al actualizar canción: ${e.localizedMessage}"))
            }
        }

        // DELETE /canciones/{id} - Eliminar canción
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de canción requerido")
            )

            try {
                val eliminado = servicioCanciones.eliminarCancion(id)
                if (eliminado) {
                    call.respond(HttpStatusCode.OK, mapOf("mensaje" to "Canción eliminada correctamente"))
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Canción no encontrada"))
                }
            } catch (e: CancionNoEncontradaException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al eliminar canción: ${e.localizedMessage}"))
            }
        }

        // GET /albumes/{albumId}/canciones - Obtener canciones por álbum
        get("/albumes/{albumId}/canciones") {
            val albumId = call.parameters["albumId"] ?: return@get call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "ID de álbum requerido")
            )

            try {
                val canciones = servicioCanciones.obtenerCancionesPorAlbum(albumId)
                val cancionesDTO = canciones.map { CancionDTO.desdeDominio(it) }
                call.respond(HttpStatusCode.OK, cancionesDTO)
            } catch (e: DatosInvalidosException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener canciones del álbum: ${e.localizedMessage}"))
            }
        }
    }
}