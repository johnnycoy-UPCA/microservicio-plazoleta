package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.EmpleadoValidacion;

public interface EmpleadoConsultaPuerto {

    EmpleadoValidacion validar(
            Long usuarioId,
            String token
    );
}