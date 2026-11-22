package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio;

import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.EventoPendienteEntidad;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EventoPendienteJpaRepositorio
        extends JpaRepository<
        EventoPendienteEntidad,
        Long> {

    List<EventoPendienteEntidad>
            findByEstadoEnvioInAndProximoIntentoLessThanEqualOrderByFechaCreacionAsc(
                    List<EstadoEnvioEvento> estados,
                    Instant fechaLimite,
                    Pageable pageable
            );
}