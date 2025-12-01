package com.musicaapi.dominio.modelos

import java.util.UUID

data class Cancion(
    val id: UUID,
    val titulo: String,
    val duracion: Int,
    val albumId: UUID
)