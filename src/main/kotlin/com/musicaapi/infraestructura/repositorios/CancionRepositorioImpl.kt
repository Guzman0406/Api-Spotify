package com.musicaapi.infraestructura.repositorios

import com.musicaapi.dominio.modelos.Cancion
import com.musicaapi.dominio.repositorios.CancionRepositorio
import com.musicaapi.infraestructura.base_de_datos.GestorBaseDatos
import com.musicaapi.infraestructura.base_de_datos.TablaCanciones
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class CancionRepositorioImpl : CancionRepositorio {

    private fun filaACancion(fila: ResultRow) = Cancion(
        id = fila[TablaCanciones.id],
        title = fila[TablaCanciones.title],
        duration = fila[TablaCanciones.duration],
        albumId = fila[TablaCanciones.albumId]
    )

    override suspend fun crear(title: String, albumId: UUID, duration: Int): Cancion =
        GestorBaseDatos.consulta {
            val nuevoId = UUID.randomUUID()
            TablaCanciones.insert {
                it[id] = nuevoId
                it[TablaCanciones.title] = title
                it[TablaCanciones.albumId] = albumId
                it[TablaCanciones.duration] = duration
            }
            TablaCanciones.select { TablaCanciones.id eq nuevoId }
                .map { filaACancion(it) }
                .single()
        }

    override suspend fun obtenerPorId(id: UUID): Cancion? =
        GestorBaseDatos.consulta {
            TablaCanciones.select { TablaCanciones.id eq id }
                .map { filaACancion(it) }
                .singleOrNull()
        }

    override suspend fun obtenerTodos(): List<Cancion> =
        GestorBaseDatos.consulta {
            TablaCanciones.selectAll().map { filaACancion(it) }
        }

    override suspend fun actualizar(id: UUID, title: String?, duration: Int?): Boolean =
        GestorBaseDatos.consulta {
            val filasActualizadas = TablaCanciones.update({ TablaCanciones.id eq id }) {
                title?.let { t -> it[TablaCanciones.title] = t }
                duration?.let { d -> it[TablaCanciones.duration] = d }
            }
            filasActualizadas > 0
        }

    override suspend fun eliminar(id: UUID): Boolean =
        GestorBaseDatos.consulta {
            TablaCanciones.deleteWhere { TablaCanciones.id eq id } > 0
        }

    override suspend fun obtenerPorAlbum(albumId: UUID): List<Cancion> =
        GestorBaseDatos.consulta {
            TablaCanciones.select { TablaCanciones.albumId eq albumId }
                .map { filaACancion(it) }
        }
}