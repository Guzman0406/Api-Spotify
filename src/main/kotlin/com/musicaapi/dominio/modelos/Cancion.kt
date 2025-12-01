package com.musicaapi.dominio.modelos

import java.util.UUID

data class Cancion(
    val id: UUID,
    val title: String,
    val duration: Int,
    val albumId: UUID
)