package com.musicaapi.presentacion.dto

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CancionDTO(
    val id: String,
    val titulo: String,
    val duracion: Int,
    val albumId: String
) {
    companion object {
        fun desdeDominio(cancion: com.musicaapi.dominio.modelos.Cancion): CancionDTO {
            return CancionDTO(
                id = cancion.id.toString(),
                titulo = cancion.titulo,
                duracion = cancion.duracion,
                albumId = cancion.albumId.toString()
            )
        }
    }
}

@Serializable
data class CrearCancionRequest(
    val titulo: String,
    val albumId: String,
    val duracion: Int
)

@Serializable
data class ActualizarCancionRequest(
    val titulo: String? = null,
    val duracion: Int? = null
)