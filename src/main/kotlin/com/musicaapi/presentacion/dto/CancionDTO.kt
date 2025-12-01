package com.musicaapi.presentacion.dto

import com.musicaapi.dominio.modelos.Cancion
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CancionDTO(
    val id: String,
    val title: String,
    val duration: Int,
    val albumId: String
) {
    companion object {
        fun desdeDominio(cancion: Cancion): CancionDTO {
            return CancionDTO(
                id = cancion.id.toString(),
                title = cancion.title,
                duration = cancion.duration,
                albumId = cancion.albumId.toString()
            )
        }
    }
}

@Serializable
data class CrearCancionRequest(
    val title: String,
    val albumId: String,
    val duration: Int
)

@Serializable
data class ActualizarCancionRequest(
    val title: String? = null,
    val duration: Int? = null
)