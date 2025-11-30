package com.musicaapi.dominio.modelos

import java.util.UUID

data class Artista(
    val id: UUID,
    val nombre: String,
    val genero: String
)