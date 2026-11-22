package co.edu.clase.microservicio_plazoleta.dominio.spi;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoPendiente;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventoPendientePersistenciaPuerto {

    EventoPendiente guardar(
            EventoPendiente eventoPendiente
    );

    Optional<EventoPendiente> buscarPorId(
            Long eventoId
    );

    List<Long> buscarIdsParaProcesar(
            List<EstadoEnvioEvento> estados,
            Instant fechaLimite,
            int maximoResultados
    );
}