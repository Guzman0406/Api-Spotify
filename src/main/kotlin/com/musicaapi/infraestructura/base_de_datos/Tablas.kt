package com.musicaapi.infraestructura.base_de_datos

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption

object TablaArtistas : Table("artistas") {
    val id = uuid("id")
    val name = varchar("name", length = 100)
    val genre = varchar("genre", length = 50)

    override val primaryKey = PrimaryKey(id)
}

object TablaAlbumes : Table("albumes") {
    val id = uuid("id")
    val title = varchar("title", length = 150)
    val releaseYear = integer("release_year")
    val artistaId = uuid("artista_id").references(TablaArtistas.id, onDelete = ReferenceOption.RESTRICT)

    override val primaryKey = PrimaryKey(id)
}

object TablaCanciones : Table("canciones") {
    val id = uuid("id")
    val title = varchar("title", length = 150)
    val duration = integer("duration")
    val albumId = uuid("album_id").references(TablaAlbumes.id, onDelete = ReferenceOption.RESTRICT)

    override val primaryKey = PrimaryKey(id)
}