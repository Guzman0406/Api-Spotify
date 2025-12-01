package com.musicaapi.presentacion.dto

import com.musicaapi.dominio.modelos.Album
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AlbumDTO(
    val id: String,
    val title: String,
    val releaseYear: Int,
    val artistId: String
) {
    companion object {
        fun desdeDominio(album: Album): AlbumDTO {
            return AlbumDTO(
                id = album.id.toString(),
                title = album.title,
                releaseYear = album.releaseYear,
                artistId = album.artistId.toString()
            )
        }
    }
}

@Serializable
data class CrearAlbumRequest(
    val title: String,
    val artistId: String,
    val releaseYear: Int
)

@Serializable
data class ActualizarAlbumRequest(
    val title: String? = null,
    val releaseYear: Int? = null
)