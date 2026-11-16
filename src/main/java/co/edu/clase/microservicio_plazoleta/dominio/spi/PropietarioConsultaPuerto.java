package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Propietario;

import java.util.Optional;

public interface PropietarioConsultaPuerto {

    Optional<Propietario> buscarPorId(Long propietarioId);
}