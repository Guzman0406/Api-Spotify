package com.musicaapi.aplicacion.servicios

import com.musicaapi.dominio.modelos.Album
import com.musicaapi.dominio.repositorios.AlbumRepositorio
import com.musicaapi.dominio.repositorios.ArtistaRepositorio
import com.musicaapi.infraestructura.excepciones.AlbumNoEncontradoException
import com.musicaapi.infraestructura.excepciones.ArtistaNoEncontradoException
import com.musicaapi.infraestructura.excepciones.DatosInvalidosException
import com.musicaapi.infraestructura.excepciones.ViolacionIntegridadException
import java.util.UUID

class ServicioAlbumes(
    private val albumRepositorio: AlbumRepositorio,
    private val artistaRepositorio: ArtistaRepositorio
) {

    suspend fun crearAlbum(title: String, artistaId: String, releaseYear: Int): Album {
        validarDatosAlbum(title, releaseYear)

        val artistaUUID = parsearUUID(artistaId) ?: throw DatosInvalidosException("ID de artista inválido: $artistaId")

        // Verificar que el artista existe
        val artista = artistaRepositorio.obtenerPorId(artistaUUID)
            ?: throw ArtistaNoEncontradoException("Artista no encontrado con ID: $artistaId")

        return albumRepositorio.crear(title, artista.id, releaseYear)
    }

    suspend fun obtenerAlbumPorId(id: String): Album {
        val albumId = parsearUUID(id) ?: throw DatosInvalidosException("ID de álbum inválido: $id")
        return albumRepositorio.obtenerPorId(albumId)
            ?: throw AlbumNoEncontradoException("Álbum no encontrado con ID: $id")
    }

    suspend fun obtenerTodosLosAlbumes(): List<Album> {
        return albumRepositorio.obtenerTodos()
    }

    suspend fun actualizarAlbum(id: String, title: String?, releaseYear: Int?): Album {
        val albumId = parsearUUID(id) ?: throw DatosInvalidosException("ID de álbum inválido: $id")

        val albumExistente = albumRepositorio.obtenerPorId(albumId)
            ?: throw AlbumNoEncontradoException("Álbum no encontrado con ID: $id")

        validarDatosActualizacion(title, releaseYear)

        val actualizado = albumRepositorio.actualizar(albumId, title, releaseYear)
        if (!actualizado) {
            throw RuntimeException("Error al actualizar el álbum")
        }

        return albumRepositorio.obtenerPorId(albumId)!!
    }

    suspend fun eliminarAlbum(id: String): Boolean {
        val albumId = parsearUUID(id) ?: throw DatosInvalidosException("ID de álbum inválido: $id")

        // Protección contra borrado en cascada
        if (albumRepositorio.tieneCanciones(albumId)) {
            throw ViolacionIntegridadException("No se puede eliminar el álbum porque tiene canciones asociadas")
        }

        return albumRepositorio.eliminar(albumId)
    }

    suspend fun obtenerAlbumesPorArtista(artistaId: String): List<Album> {
        val artistaUUID = parsearUUID(artistaId) ?: throw DatosInvalidosException("ID de artista inválido: $artistaId")
        return albumRepositorio.obtenerPorArtista(artistaUUID)
    }

    private fun validarDatosAlbum(title: String, releaseYear: Int) {
        if (title.isBlank()) {
            throw DatosInvalidosException("El título del álbum no puede estar vacío")
        }
        if (title.length > 150) {
            throw DatosInvalidosException("El título del álbum no puede tener más de 150 caracteres")
        }
        if (releaseYear < 1900 || releaseYear > 2100) {
            throw DatosInvalidosException("El año de lanzamiento debe estar entre 1900 y 2100")
        }
    }

    private fun validarDatosActualizacion(title: String?, releaseYear: Int?) {
        title?.let {
            if (it.isBlank()) throw DatosInvalidosException("El título del álbum no puede estar vacío")
            if (it.length > 150) throw DatosInvalidosException("El título del álbum no puede tener más de 150 caracteres")
        }
        releaseYear?.let {
            if (it < 1900 || it > 2100) throw DatosInvalidosException("El año de lanzamiento debe estar entre 1900 y 2100")
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