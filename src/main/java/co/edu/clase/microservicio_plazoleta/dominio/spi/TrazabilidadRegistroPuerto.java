package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;

public interface TrazabilidadRegistroPuerto {

    void registrar(EventoTrazabilidad evento);
}
