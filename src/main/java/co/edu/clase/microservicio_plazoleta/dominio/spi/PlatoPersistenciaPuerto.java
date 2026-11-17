package co.edu.clase.microservicio_plazoleta.dominio.spi;

import java.util.Optional;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;

public interface PlatoPersistenciaPuerto {

    Optional<Plato> buscarPorId(Long platoId);
    
    Plato guardar(Plato plato);
}
