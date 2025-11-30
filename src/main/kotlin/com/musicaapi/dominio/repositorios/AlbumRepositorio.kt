package com.musicaapi.dominio.repositorios

import com.musicaapi.dominio.modelos.Album
import java.util.UUID

interface AlbumRepositorio {
    suspend fun crear(titulo: String, artistaId: UUID, añoLanzamiento: Int): Album
    suspend fun obtenerPorId(id: UUID): Album?
    suspend fun obtenerTodos(): List<Album>
    suspend fun actualizar(id: UUID, titulo: String?, añoLanzamiento: Int?): Boolean
    suspend fun eliminar(id: UUID): Boolean
    suspend fun tieneCanciones(id: UUID): Boolean
    suspend fun obtenerPorArtista(artistaId: UUID): List<Album>
}