package com.musicaapi.presentacion.dto

import com.musicaapi.dominio.modelos.Album
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AlbumDTO(
    val id: String,
    val title: String,
    val releaseYear: Int,
    val artistaId: String
) {
    companion object {
        fun desdeDominio(album: Album): AlbumDTO {
            return AlbumDTO(
                id = album.id.toString(),
                title = album.title,
                releaseYear = album.releaseYear,
                artistaId = album.artistaId.toString()
            )
        }
    }
}

@Serializable
data class CrearAlbumRequest(
    val title: String,
    val artistaId: String,
    val releaseYear: Int
)

@Serializable
data class ActualizarAlbumRequest(
    val title: String? = null,
    val releaseYear: Int? = null
)