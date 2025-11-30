package com.musicaapi.aplicacion.servicios

import com.musicaapi.dominio.modelos.Artista
import com.musicaapi.dominio.repositorios.ArtistaRepositorio
import com.musicaapi.infraestructura.excepciones.ArtistaNoEncontradoException
import com.musicaapi.infraestructura.excepciones.DatosInvalidosException
import com.musicaapi.infraestructura.excepciones.ViolacionIntegridadException
import java.util.UUID

class ServicioArtistas(
    private val artistaRepositorio: ArtistaRepositorio
) {

    suspend fun crearArtista(nombre: String, genero: String): Artista {
        validarDatosArtista(nombre, genero)
        return artistaRepositorio.crear(nombre, genero)
    }

    suspend fun obtenerArtistaPorId(id: String): Artista {
        val artistaId = parsearUUID(id) ?: throw DatosInvalidosException("ID de artista inválido: $id")
        return artistaRepositorio.obtenerPorId(artistaId)
            ?: throw ArtistaNoEncontradoException("Artista no encontrado con ID: $id")
    }

    suspend fun obtenerTodosLosArtistas(): List<Artista> {
        return artistaRepositorio.obtenerTodos()
    }

    suspend fun actualizarArtista(id: String, nombre: String?, genero: String?): Artista {
        val artistaId = parsearUUID(id) ?: throw DatosInvalidosException("ID de artista inválido: $id")

        val artistaExistente = artistaRepositorio.obtenerPorId(artistaId)
            ?: throw ArtistaNoEncontradoException("Artista no encontrado con ID: $id")

        validarDatosActualizacion(nombre, genero)

        val actualizado = artistaRepositorio.actualizar(artistaId, nombre, genero)
        if (!actualizado) {
            throw RuntimeException("Error al actualizar el artista")
        }

        return artistaRepositorio.obtenerPorId(artistaId)!!
    }

    suspend fun eliminarArtista(id: String): Boolean {
        val artistaId = parsearUUID(id) ?: throw DatosInvalidosException("ID de artista inválido: $id")

        // Protección contra borrado en cascada
        if (artistaRepositorio.tieneAlbumes(artistaId)) {
            throw ViolacionIntegridadException("No se puede eliminar el artista porque tiene álbumes asociados")
        }

        return artistaRepositorio.eliminar(artistaId)
    }

    private fun validarDatosArtista(nombre: String, genero: String) {
        if (nombre.isBlank()) {
            throw DatosInvalidosException("El nombre del artista no puede estar vacío")
        }
        if (genero.isBlank()) {
            throw DatosInvalidosException("El género del artista no puede estar vacío")
        }
        if (nombre.length > 100) {
            throw DatosInvalidosException("El nombre del artista no puede tener más de 100 caracteres")
        }
        if (genero.length > 50) {
            throw DatosInvalidosException("El género del artista no puede tener más de 50 caracteres")
        }
    }

    private fun validarDatosActualizacion(nombre: String?, genero: String?) {
        nombre?.let {
            if (it.isBlank()) throw DatosInvalidosException("El nombre del artista no puede estar vacío")
            if (it.length > 100) throw DatosInvalidosException("El nombre del artista no puede tener más de 100 caracteres")
        }
        genero?.let {
            if (it.isBlank()) throw DatosInvalidosException("El género del artista no puede estar vacío")
            if (it.length > 50) throw DatosInvalidosException("El género del artista no puede tener más de 50 caracteres")
        }
    }

    private fun parsearUUID(id: String): UUID? {
        return try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}