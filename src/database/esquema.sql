-- Script de inicialización de la base de datos para la API de Música
-- Ejecutar este script en PostgreSQL antes de iniciar la aplicación

-- Crear base de datos (ejecutar separadamente)
-- CREATE DATABASE musica_db;

-- Habilitar extensión UUID si no está disponible
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabla de Artistas
CREATE TABLE IF NOT EXISTS artistas (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    creado_en TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                            );

-- Tabla de Álbumes
CREATE TABLE IF NOT EXISTS albumes (
                                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    titulo VARCHAR(150) NOT NULL,
    año_lanzamiento INTEGER NOT NULL CHECK (año_lanzamiento >= 1900 AND año_lanzamiento <= 2100),
    artista_id UUID NOT NULL,
    creado_en TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_artista
                            FOREIGN KEY (artista_id)
    REFERENCES artistas(id)
                        ON DELETE RESTRICT
    );

-- Tabla de Canciones
CREATE TABLE IF NOT EXISTS canciones (
                                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    titulo VARCHAR(150) NOT NULL,
    duracion INTEGER NOT NULL CHECK (duracion > 0 AND duracion <= 3600),
    album_id UUID NOT NULL,
    creado_en TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_album
                            FOREIGN KEY (album_id)
    REFERENCES albumes(id)
                        ON DELETE RESTRICT
    );

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_albumes_artista_id ON albumes(artista_id);
CREATE INDEX IF NOT EXISTS idx_canciones_album_id ON canciones(album_id);
CREATE INDEX IF NOT EXISTS idx_artistas_nombre ON artistas(nombre);
CREATE INDEX IF NOT EXISTS idx_albumes_titulo ON albumes(titulo);

-- Datos de ejemplo (opcional)
INSERT INTO artistas (nombre, genero) VALUES
                                          ('Los Rodríguez', 'Rock'),
                                          ('La Banda Ejemplo', 'Pop'),
                                          ('El Solista', 'Balada');

INSERT INTO albumes (titulo, año_lanzamiento, artista_id)
SELECT 'Álbum Debut', 2020, id FROM artistas WHERE nombre = 'Los Rodríguez'
UNION ALL
SELECT 'Noche Estrellada', 2022, id FROM artistas WHERE nombre = 'La Banda Ejemplo'
UNION ALL
SELECT 'Sueños', 2021, id FROM artistas WHERE nombre = 'El Solista';

INSERT INTO canciones (titulo, duracion, album_id)
SELECT 'Canción Principal', 240, id FROM albumes WHERE titulo = 'Álbum Debut'
UNION ALL
SELECT 'Melodía Nocturna', 180, id FROM albumes WHERE titulo = 'Noche Estrellada'
UNION ALL
SELECT 'Sueño Dorado', 210, id FROM albumes WHERE titulo = 'Sueños';

-- Verificar datos insertados
SELECT '✅ Base de datos configurada correctamente' as mensaje;