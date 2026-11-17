CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(150) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_categorias_nombre UNIQUE (nombre)
);

CREATE TABLE platos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(300) NOT NULL,
    precio INTEGER NOT NULL,
    url_imagen VARCHAR(500) NOT NULL,
    categoria_id BIGINT NOT NULL,
    restaurante_id BIGINT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_platos_categorias
        FOREIGN KEY (categoria_id)
        REFERENCES categorias (id),

    CONSTRAINT fk_platos_restaurantes
        FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes (id),

    CONSTRAINT ck_platos_precio_positivo
        CHECK (precio > 0)
);

CREATE INDEX idx_platos_restaurante
    ON platos (restaurante_id);

CREATE INDEX idx_platos_categoria
    ON platos (categoria_id);

CREATE INDEX idx_platos_restaurante_categoria_activo
    ON platos (
        restaurante_id,
        categoria_id,
        activo
    );

INSERT INTO categorias (
    nombre,
    descripcion,
    activo
)
VALUES
    (
        'ENTRADA',
        'Platos servidos antes del plato principal',
        TRUE
    ),
    (
        'PLATO_FUERTE',
        'Platos principales del menú',
        TRUE
    ),
    (
        'POSTRE',
        'Preparaciones dulces',
        TRUE
    ),
    (
        'BEBIDA',
        'Bebidas frías o calientes',
        TRUE
    );