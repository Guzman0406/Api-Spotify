package com.musicaapi.infraestructura.repositorios

import com.musicaapi.dominio.modelos.Artista
import com.musicaapi.dominio.repositorios.ArtistaRepositorio
import com.musicaapi.infraestructura.base_de_datos.GestorBaseDatos
import com.musicaapi.infraestructura.base_de_datos.TablaArtistas
import com.musicaapi.infraestructura.base_de_datos.TablaAlbumes
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class ArtistaRepositorioImpl : ArtistaRepositorio {

    private fun filaAArtista(fila: ResultRow) = Artista(
        id = fila[TablaArtistas.id],
        nombre = fila[TablaArtistas.nombre],
        genero = fila[TablaArtistas.genero]
    )

    override suspend fun crear(nombre: String, genero: String): Artista =
        GestorBaseDatos.consulta {
            val nuevoId = UUID.randomUUID()
            TablaArtistas.insert {
                it[id] = nuevoId
                it[TablaArtistas.nombre] = nombre
                it[TablaArtistas.genero] = genero
            }
            TablaArtistas.select { TablaArtistas.id eq nuevoId }
                .map { filaAArtista(it) }
                .single()
        }

    override suspend fun obtenerPorId(id: UUID): Artista? =
        GestorBaseDatos.consulta {
            TablaArtistas.select { TablaArtistas.id eq id }
                .map { filaAArtista(it) }
                .singleOrNull()
        }

    override suspend fun obtenerTodos(): List<Artista> =
        GestorBaseDatos.consulta {
            TablaArtistas.selectAll().map { filaAArtista(it) }
        }

    override suspend fun actualizar(id: UUID, nombre: String?, genero: String?): Boolean =
        GestorBaseDatos.consulta {
            val filasActualizadas = TablaArtistas.update({ TablaArtistas.id eq id }) {
                nombre?.let { n -> it[TablaArtistas.nombre] = n }
                genero?.let { g -> it[TablaArtistas.genero] = g }
            }
            filasActualizadas > 0
        }

    override suspend fun eliminar(id: UUID): Boolean =
        GestorBaseDatos.consulta {
            TablaArtistas.deleteWhere { TablaArtistas.id eq id } > 0
        }

    override suspend fun tieneAlbumes(id: UUID): Boolean =
        GestorBaseDatos.consulta {
            TablaAlbumes.select { TablaAlbumes.artistaId eq id }.count() > 0
        }
}