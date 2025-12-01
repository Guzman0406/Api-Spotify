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
        titulo = fila[TablaCanciones.titulo],
        duracion = fila[TablaCanciones.duracion],
        albumId = fila[TablaCanciones.albumId]
    )

    override suspend fun crear(titulo: String, albumId: UUID, duracion: Int): Cancion =
        GestorBaseDatos.consulta {
            val nuevoId = UUID.randomUUID()
            TablaCanciones.insert {
                it[id] = nuevoId
                it[TablaCanciones.titulo] = titulo
                it[TablaCanciones.albumId] = albumId
                it[TablaCanciones.duracion] = duracion
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

    override suspend fun actualizar(id: UUID, titulo: String?, duracion: Int?): Boolean =
        GestorBaseDatos.consulta {
            val filasActualizadas = TablaCanciones.update({ TablaCanciones.id eq id }) {
                titulo?.let { t -> it[TablaCanciones.titulo] = t }
                duracion?.let { d -> it[TablaCanciones.duracion] = d }
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