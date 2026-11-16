CREATE TABLE restaurantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nit VARCHAR(20) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    celular VARCHAR(13) NOT NULL,
    url_logo VARCHAR(500) NOT NULL,
    propietario_id BIGINT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_restaurantes_nit UNIQUE (nit)
);

CREATE INDEX idx_restaurantes_propietario
    ON restaurantes (propietario_id);

CREATE INDEX idx_restaurantes_nombre
    ON restaurantes (nombre);