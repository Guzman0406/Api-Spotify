package com.musicaapi.aplicacion.servicios

import com.musicaapi.dominio.modelos.Cancion
import com.musicaapi.dominio.repositorios.AlbumRepositorio
import com.musicaapi.dominio.repositorios.CancionRepositorio
import com.musicaapi.infraestructura.excepciones.AlbumNoEncontradoException
import com.musicaapi.infraestructura.excepciones.CancionNoEncontradaException
import com.musicaapi.infraestructura.excepciones.DatosInvalidosException
import java.util.UUID

class ServicioCanciones(
    private val cancionRepositorio: CancionRepositorio,
    private val albumRepositorio: AlbumRepositorio
) {

    suspend fun crearCancion(title: String, albumId: String, duration: Int): Cancion {
        validarDatosCancion(title, duration)

        val albumUUID = parsearUUID(albumId) ?: throw DatosInvalidosException("ID de álbum inválido: $albumId")

        // Verificar que el álbum existe
        val album = albumRepositorio.obtenerPorId(albumUUID)
            ?: throw AlbumNoEncontradoException("Álbum no encontrado con ID: $albumId")

        return cancionRepositorio.crear(title, album.id, duration)
    }

    suspend fun obtenerCancionPorId(id: String): Cancion {
        val cancionId = parsearUUID(id) ?: throw DatosInvalidosException("ID de canción inválido: $id")
        return cancionRepositorio.obtenerPorId(cancionId)
            ?: throw CancionNoEncontradaException("Canción no encontrada con ID: $id")
    }

    suspend fun obtenerTodasLasCanciones(): List<Cancion> {
        return cancionRepositorio.obtenerTodos()
    }

    suspend fun actualizarCancion(id: String, title: String?, duration: Int?): Cancion {
        val cancionId = parsearUUID(id) ?: throw DatosInvalidosException("ID de canción inválido: $id")

        val cancionExistente = cancionRepositorio.obtenerPorId(cancionId)
            ?: throw CancionNoEncontradaException("Canción no encontrada con ID: $id")

        validarDatosActualizacion(title, duration)

        val actualizado = cancionRepositorio.actualizar(cancionId, title, duration)
        if (!actualizado) {
            throw RuntimeException("Error al actualizar la canción")
        }

        return cancionRepositorio.obtenerPorId(cancionId)!!
    }

    suspend fun eliminarCancion(id: String): Boolean {
        val cancionId = parsearUUID(id) ?: throw DatosInvalidosException("ID de canción inválido: $id")
        return cancionRepositorio.eliminar(cancionId)
    }

    suspend fun obtenerCancionesPorAlbum(albumId: String): List<Cancion> {
        val albumUUID = parsearUUID(albumId) ?: throw DatosInvalidosException("ID de álbum inválido: $albumId")
        return cancionRepositorio.obtenerPorAlbum(albumUUID)
    }

    private fun validarDatosCancion(title: String, duration: Int) {
        if (title.isBlank()) {
            throw DatosInvalidosException("El título de la canción no puede estar vacío")
        }
        if (title.length > 150) {
            throw DatosInvalidosException("El título de la canción no puede tener más de 150 caracteres")
        }
        if (duration <= 0) {
            throw DatosInvalidosException("La duración de la canción debe ser mayor a 0 segundos")
        }
        if (duration > 3600) { // 1 hora máxima
            throw DatosInvalidosException("La duración de la canción no puede ser mayor a 3600 segundos")
        }
    }

    private fun validarDatosActualizacion(title: String?, duration: Int?) {
        title?.let {
            if (it.isBlank()) throw DatosInvalidosException("El título de la canción no puede estar vacío")
            if (it.length > 150) throw DatosInvalidosException("El título de la canción no puede tener más de 150 caracteres")
        }
        duration?.let {
            if (it <= 0) throw DatosInvalidosException("La duración de la canción debe ser mayor a 0 segundos")
            if (it > 3600) throw DatosInvalidosException("La duración de la canción no puede ser mayor a 3600 segundos")
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