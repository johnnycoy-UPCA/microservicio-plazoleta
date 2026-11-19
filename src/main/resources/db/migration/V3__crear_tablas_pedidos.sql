CREATE TABLE pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    restaurante_id BIGINT NOT NULL,
    empleado_asignado_id BIGINT NULL,
    estado VARCHAR(30) NOT NULL,
    total BIGINT NOT NULL,
    pin_hash VARCHAR(255) NULL,
    pin_generado_en TIMESTAMP NULL,
    pin_expira_en TIMESTAMP NULL,
    pin_intentos_fallidos INTEGER NOT NULL DEFAULT 0,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_pedidos_restaurantes
        FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes (id),

    CONSTRAINT ck_pedidos_total_positivo
        CHECK (total > 0),

    CONSTRAINT ck_pedidos_intentos_pin
        CHECK (pin_intentos_fallidos >= 0)
);

CREATE TABLE pedidos_platos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    plato_id BIGINT NOT NULL,
    nombre_plato VARCHAR(100) NOT NULL,
    precio_unitario INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,

    CONSTRAINT fk_pedidos_platos_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedidos (id),

    CONSTRAINT fk_pedidos_platos_plato
        FOREIGN KEY (plato_id)
        REFERENCES platos (id),

    CONSTRAINT uk_pedidos_platos_pedido_plato
        UNIQUE (pedido_id, plato_id),

    CONSTRAINT ck_pedidos_platos_precio
        CHECK (precio_unitario > 0),

    CONSTRAINT ck_pedidos_platos_cantidad
        CHECK (cantidad BETWEEN 1 AND 100)
);

CREATE INDEX idx_pedidos_cliente_estado
    ON pedidos (cliente_id, estado);

CREATE INDEX idx_pedidos_restaurante_estado_fecha
    ON pedidos (
        restaurante_id,
        estado,
        fecha_creacion
    );

CREATE INDEX idx_pedidos_empleado_estado
    ON pedidos (
        empleado_asignado_id,
        estado
    );

CREATE INDEX idx_pedidos_platos_pedido
    ON pedidos_platos (pedido_id);