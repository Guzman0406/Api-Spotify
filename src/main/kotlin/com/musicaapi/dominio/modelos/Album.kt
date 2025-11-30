package com.musicaapi.dominio.modelos

import java.util.UUID

data class Album(
    val id: UUID,
    val titulo: String,
    val añoLanzamiento: Int,
    val artistaId: UUID
)