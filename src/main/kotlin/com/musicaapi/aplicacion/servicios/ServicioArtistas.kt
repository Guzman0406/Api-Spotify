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

    suspend fun crearArtista(name: String, genre: String): Artista {
        validarDatosArtista(name, genre)
        return artistaRepositorio.crear(name, genre)
    }

    suspend fun obtenerArtistaPorId(id: String): Artista {
        val artistaId = parsearUUID(id) ?: throw DatosInvalidosException("ID de artista inválido: $id")
        return artistaRepositorio.obtenerPorId(artistaId)
            ?: throw ArtistaNoEncontradoException("Artista no encontrado con ID: $id")
    }

    suspend fun obtenerTodosLosArtistas(): List<Artista> {
        return artistaRepositorio.obtenerTodos()
    }

    suspend fun actualizarArtista(id: String, name: String?, genre: String?): Artista {
        val artistaId = parsearUUID(id) ?: throw DatosInvalidosException("ID de artista inválido: $id")

        artistaRepositorio.obtenerPorId(artistaId)
            ?: throw ArtistaNoEncontradoException("Artista no encontrado con ID: $id")

        validarDatosActualizacion(name, genre)

        val actualizado = artistaRepositorio.actualizar(artistaId, name, genre)
        if (!actualizado) {
            // Esta excepción es genérica porque si el artista existe, la actualización no debería fallar.
            // Si falla, es un error inesperado del sistema.
            throw RuntimeException("Error al actualizar el artista con ID: $id")
        }

        return artistaRepositorio.obtenerPorId(artistaId)!!
    }

    suspend fun eliminarArtista(id: String): Boolean {
        val artistaId = parsearUUID(id) ?: throw DatosInvalidosException("ID de artista inválido: $id")

        // 1. Verificar que el artista existe
        artistaRepositorio.obtenerPorId(artistaId)
            ?: throw ArtistaNoEncontradoException("No se puede eliminar, artista no encontrado con ID: $id")

        // 2. Verificar si tiene álbumes asociados (protección de integridad)
        if (artistaRepositorio.tieneAlbumes(artistaId)) {
            throw ViolacionIntegridadException("No se puede eliminar el artista porque tiene álbumes asociados.")
        }

        // 3. Si todo está bien, eliminar
        return artistaRepositorio.eliminar(artistaId)
    }

    private fun validarDatosArtista(name: String, genre: String) {
        if (name.isBlank()) {
            throw DatosInvalidosException("El nombre del artista no puede estar vacío")
        }
        if (genre.isBlank()) {
            throw DatosInvalidosException("El género del artista no puede estar vacío")
        }
        if (name.length > 100) {
            throw DatosInvalidosException("El nombre del artista no puede tener más de 100 caracteres")
        }
        if (genre.length > 50) {
            throw DatosInvalidosException("El género del artista no puede tener más de 50 caracteres")
        }
    }

    private fun validarDatosActualizacion(name: String?, genre: String?) {
        name?.let {
            if (it.isBlank()) throw DatosInvalidosException("El nombre del artista no puede estar vacío")
            if (it.length > 100) throw DatosInvalidosException("El nombre del artista no puede tener más de 100 caracteres")
        }
        genre?.let {
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