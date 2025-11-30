package com.musicaapi.infraestructura.excepciones

class ArtistaNoEncontradoException(mensaje: String) : RuntimeException(mensaje)
class AlbumNoEncontradoException(mensaje: String) : RuntimeException(mensaje)
class CancionNoEncontradaException(mensaje: String) : RuntimeException(mensaje)
class DatosInvalidosException(mensaje: String) : RuntimeException(mensaje)
class ViolacionIntegridadException(mensaje: String) : RuntimeException(mensaje)