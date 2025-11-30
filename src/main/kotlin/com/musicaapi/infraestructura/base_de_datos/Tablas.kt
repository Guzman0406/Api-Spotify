package com.musicaapi.infraestructura.base_de_datos

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption

object TablaArtistas : Table("artistas") {
    val id = uuid("id")
    val nombre = varchar("nombre", length = 100)
    val genero = varchar("genero", length = 50)

    override val primaryKey = PrimaryKey(id)
}

object TablaAlbumes : Table("albumes") {
    val id = uuid("id")
    val titulo = varchar("titulo", length = 150)
    val añoLanzamiento = integer("año_lanzamiento")
    val artistaId = uuid("artista_id").references(TablaArtistas.id, onDelete = ReferenceOption.RESTRICT)

    override val primaryKey = PrimaryKey(id)
}

object TablaCanciones : Table("canciones") {
    val id = uuid("id")
    val titulo = varchar("titulo", length = 150)
    val duracion = integer("duracion")
    val albumId = uuid("album_id").references(TablaAlbumes.id, onDelete = ReferenceOption.RESTRICT)

    override val primaryKey = PrimaryKey(id)
}