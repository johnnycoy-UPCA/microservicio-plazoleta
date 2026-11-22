package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;

public interface EventoSerializadorPuerto {

    String serializar(
        EventoTrazabilidad evento
    );

    EventoTrazabilidad deserializar(
        String cargaJson
    );
}
