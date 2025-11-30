package com.musicaapi.presentacion.dto

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AlbumDTO(
    val id: String,
    val titulo: String,
    val añoLanzamiento: Int,
    val artistaId: String
) {
    companion object {
        fun desdeDominio(album: com.musicaapi.dominio.modelos.Album): AlbumDTO {
            return AlbumDTO(
                id = album.id.toString(),
                titulo = album.titulo,
                añoLanzamiento = album.añoLanzamiento,
                artistaId = album.artistaId.toString()
            )
        }
    }
}

@Serializable
data class CrearAlbumRequest(
    val titulo: String,
    val artistaId: String,
    val añoLanzamiento: Int
)

@Serializable
data class ActualizarAlbumRequest(
    val titulo: String? = null,
    val añoLanzamiento: Int? = null
)