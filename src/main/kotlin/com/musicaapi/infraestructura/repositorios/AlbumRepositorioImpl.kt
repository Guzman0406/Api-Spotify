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
        title = fila[TablaAlbumes.title],
        releaseYear = fila[TablaAlbumes.releaseYear],
        artistId = fila[TablaAlbumes.artistId]
    )

    override suspend fun crear(title: String, artistId: UUID, releaseYear: Int): Album =
        GestorBaseDatos.consulta {
            val nuevoId = UUID.randomUUID()
            TablaAlbumes.insert {
                it[id] = nuevoId
                it[TablaAlbumes.title] = title
                it[TablaAlbumes.artistId] = artistId
                it[TablaAlbumes.releaseYear] = releaseYear
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

    override suspend fun actualizar(id: UUID, title: String?, releaseYear: Int?): Boolean =
        GestorBaseDatos.consulta {
            val filasActualizadas = TablaAlbumes.update({ TablaAlbumes.id eq id }) {
                title?.let { t -> it[TablaAlbumes.title] = t }
                releaseYear?.let { a -> it[TablaAlbumes.releaseYear] = a }
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

    override suspend fun obtenerPorArtista(artistId: UUID): List<Album> =
        GestorBaseDatos.consulta {
            TablaAlbumes.select { TablaAlbumes.artistId eq artistId }
                .map { filaAAlbum(it) }
        }
}