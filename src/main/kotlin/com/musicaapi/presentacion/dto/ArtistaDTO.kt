package com.musicaapi.presentacion.dto

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ArtistaDTO(
    val id: String,
    val nombre: String,
    val genero: String
) {
    companion object {
        fun desdeDominio(artista: com.musicaapi.dominio.modelos.Artista): ArtistaDTO {
            return ArtistaDTO(
                id = artista.id.toString(),
                nombre = artista.nombre,
                genero = artista.genero
            )
        }
    }
}

@Serializable
data class CrearArtistaRequest(
    val nombre: String,
    val genero: String
)

@Serializable
data class ActualizarArtistaRequest(
    val nombre: String? = null,
    val genero: String? = null
)