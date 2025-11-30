package com.musicaapi.dominio.repositorios

import com.musicaapi.dominio.modelos.Artista
import java.util.UUID

interface ArtistaRepositorio {
    suspend fun crear(nombre: String, genero: String): Artista
    suspend fun obtenerPorId(id: UUID): Artista?
    suspend fun obtenerTodos(): List<Artista>
    suspend fun actualizar(id: UUID, nombre: String?, genero: String?): Boolean
    suspend fun eliminar(id: UUID): Boolean
    suspend fun tieneAlbumes(id: UUID): Boolean
}