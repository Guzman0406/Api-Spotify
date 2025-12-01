package com.musicaapi.dominio.repositorios

import com.musicaapi.dominio.modelos.Album
import java.util.UUID

interface AlbumRepositorio {
    suspend fun crear(title: String, artistaId: UUID, releaseYear: Int): Album
    suspend fun obtenerPorId(id: UUID): Album?
    suspend fun obtenerTodos(): List<Album>
    suspend fun actualizar(id: UUID, title: String?, releaseYear: Int?): Boolean
    suspend fun eliminar(id: UUID): Boolean
    suspend fun tieneCanciones(id: UUID): Boolean
    suspend fun obtenerPorArtista(artistaId: UUID): List<Album>
}