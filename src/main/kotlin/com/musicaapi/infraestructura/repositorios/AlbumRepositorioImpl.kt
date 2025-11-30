package com.musicaapi.infraestructura.repositorios

import com.musicaapi.dominio.modelos.Album
import com.musicaapi.dominio.repositorios.AlbumRepositorio
import com.musicaapi.infraestructura.base_de_datos.GestorBaseDatos
import com.musicaapi.infraestructura.base_de_datos.TablaAlbumes
import com.musicaapi.infraestructura.base_de_datos.TablaCanciones
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class AlbumRepositorioImpl : AlbumRepositorio {

    private fun filaAAlbum(fila: ResultRow) = Album(
        id = fila[TablaAlbumes.id],
        titulo = fila[TablaAlbumes.titulo],
        añoLanzamiento = fila[TablaAlbumes.añoLanzamiento],
        artistaId = fila[TablaAlbumes.artistaId]
    )

    override suspend fun crear(titulo: String, artistaId: UUID, añoLanzamiento: Int): Album =
        GestorBaseDatos.consulta {
            val nuevoId = UUID.randomUUID()
            TablaAlbumes.insert {
                it[id] = nuevoId
                it[TablaAlbumes.titulo] = titulo
                it[TablaAlbumes.artistaId] = artistaId
                it[TablaAlbumes.añoLanzamiento] = añoLanzamiento
            }
            TablaAlbumes.select { TablaAlbumes.id eq nuevoId }
                .map { filaAAlbum(it) }
                .single()
        }

    override suspend fun obtenerPorId(id: UUID): Album? =
        GestorBaseDatos.consulta {
            TablaAlbumes.select { TablaAlbumes.id eq id }
                .map { filaAAlbum(it) }
                .singleOrNull()
        }

    override suspend fun obtenerTodos(): List<Album> =
        GestorBaseDatos.consulta {
            TablaAlbumes.selectAll().map { filaAAlbum(it) }
        }

    override suspend fun actualizar(id: UUID, titulo: String?, añoLanzamiento: Int?): Boolean =
        GestorBaseDatos.consulta {
            val filasActualizadas = TablaAlbumes.update({ TablaAlbumes.id eq id }) {
                titulo?.let { t -> it[TablaAlbumes.titulo] = t }
                añoLanzamiento?.let { a -> it[TablaAlbumes.añoLanzamiento] = a }
            }
            filasActualizadas > 0
        }

    override suspend fun eliminar(id: UUID): Boolean =
        GestorBaseDatos.consulta {
            TablaAlbumes.deleteWhere { TablaAlbumes.id eq id } > 0
        }

    override suspend fun tieneCanciones(id: UUID): Boolean =
        GestorBaseDatos.consulta {
            TablaCanciones.select { TablaCanciones.albumId eq id }.count() > 0
        }

    override suspend fun obtenerPorArtista(artistaId: UUID): List<Album> =
        GestorBaseDatos.consulta {
            TablaAlbumes.select { TablaAlbumes.artistaId eq artistaId }
                .map { filaAAlbum(it) }
        }
}