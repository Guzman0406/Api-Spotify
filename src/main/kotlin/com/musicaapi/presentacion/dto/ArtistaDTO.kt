package com.musicaapi.presentacion.dto

import com.musicaapi.dominio.modelos.Artista
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ArtistaDTO(
    val id: String,
    val name: String,
    val genre: String
) {
    companion object {
        fun desdeDominio(artista: Artista): ArtistaDTO {
            return ArtistaDTO(
                id = artista.id.toString(),
                name = artista.name,
                genre = artista.genre
            )
        }
    }
}

@Serializable
data class CrearArtistaRequest(
    val name: String,
    val genre: String
)

@Serializable
data class ActualizarArtistaRequest(
    val name: String? = null,
    val genre: String? = null
)