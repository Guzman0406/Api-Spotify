package com.musicaapi.dominio.repositorios

import com.musicaapi.dominio.modelos.Artista
import java.util.UUID

interface ArtistaRepositorio {
    suspend fun crear(name: String, genre: String): Artista
    suspend fun obtenerPorId(id: UUID): Artista?
    suspend fun obtenerTodos(): List<Artista>
    suspend fun actualizar(id: UUID, name: String?, genre: String?): Boolean
    suspend fun eliminar(id: UUID): Boolean
    suspend fun tieneAlbumes(id: UUID): Boolean
}