package com.musicaapi.dominio.repositorios

import com.musicaapi.dominio.modelos.Cancion
import java.util.UUID

interface CancionRepositorio {
    suspend fun crear(titulo: String, albumId: UUID, duracion: Int): Cancion
    suspend fun obtenerPorId(id: UUID): Cancion?
    suspend fun obtenerTodos(): List<Cancion>
    suspend fun actualizar(id: UUID, titulo: String?, duracion: Int?): Boolean
    suspend fun eliminar(id: UUID): Boolean
    suspend fun obtenerPorAlbum(albumId: UUID): List<Cancion>
}