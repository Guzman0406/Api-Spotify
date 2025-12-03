CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Artistas
CREATE TABLE IF NOT EXISTS artistas (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    creado_en TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                            );

-- Álbumes
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

-- Canciones
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


CREATE INDEX IF NOT EXISTS idx_albumes_artista_id ON albumes(artista_id);
CREATE INDEX IF NOT EXISTS idx_canciones_album_id ON canciones(album_id);
CREATE INDEX IF NOT EXISTS idx_artistas_nombre ON artistas(nombre);
CREATE INDEX IF NOT EXISTS idx_albumes_titulo ON albumes(titulo);

