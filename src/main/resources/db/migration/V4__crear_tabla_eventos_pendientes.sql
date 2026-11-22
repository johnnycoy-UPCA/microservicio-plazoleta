CREATE TABLE eventos_pendientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_evento VARCHAR(80) NOT NULL,
    agregado_id BIGINT NOT NULL,
    carga_json CLOB NOT NULL,
    estado_envio VARCHAR(20) NOT NULL,
    intentos INTEGER NOT NULL DEFAULT 0,
    proximo_intento TIMESTAMP NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_envio TIMESTAMP NULL,
    ultimo_error VARCHAR(500) NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uk_eventos_pendientes_tipo_agregado
        UNIQUE (tipo_evento, agregado_id),

    CONSTRAINT ck_eventos_pendientes_intentos
        CHECK (intentos >= 0)
);

CREATE INDEX idx_eventos_pendientes_estado_proximo
    ON eventos_pendientes (
        estado_envio,
        proximo_intento
    );