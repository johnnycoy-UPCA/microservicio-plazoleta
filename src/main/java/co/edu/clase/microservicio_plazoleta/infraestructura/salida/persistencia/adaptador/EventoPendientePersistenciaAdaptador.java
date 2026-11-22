package co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoPendiente;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoPendientePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.entidad.EventoPendienteEntidad;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.mapeador.EventoPendientePersistenciaMapeador;
import co.edu.clase.microservicio_plazoleta.infraestructura.salida.persistencia.repositorio.EventoPendienteJpaRepositorio;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EventoPendientePersistenciaAdaptador
        implements EventoPendientePersistenciaPuerto {

    private final EventoPendienteJpaRepositorio
            eventoPendienteJpaRepositorio;

    private final EventoPendientePersistenciaMapeador
            eventoPendientePersistenciaMapeador;

    public EventoPendientePersistenciaAdaptador(
            EventoPendienteJpaRepositorio
                    eventoPendienteJpaRepositorio,
            EventoPendientePersistenciaMapeador
                    eventoPendientePersistenciaMapeador) {

        this.eventoPendienteJpaRepositorio =
                eventoPendienteJpaRepositorio;

        this.eventoPendientePersistenciaMapeador =
                eventoPendientePersistenciaMapeador;
    }

    @Override
    public EventoPendiente guardar(
            EventoPendiente eventoPendiente) {

        EventoPendienteEntidad entidad =
                eventoPendientePersistenciaMapeador
                        .aEntidad(eventoPendiente);

        EventoPendienteEntidad guardada =
                eventoPendienteJpaRepositorio
                        .save(entidad);

        return eventoPendientePersistenciaMapeador
                .aModelo(guardada);
    }

	@Override
	@Transactional(readOnly = true)
	public Optional<EventoPendiente> buscarPorId(
			Long eventoId) {

		return eventoPendienteJpaRepositorio
				.findById(eventoId)
				.map(
						eventoPendientePersistenciaMapeador
								::aModelo
				);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> buscarIdsParaProcesar(
			List<EstadoEnvioEvento> estados,
			Instant fechaLimite,
			int maximoResultados) {

		// return eventoPendienteJpaRepositorio
		// 		.findByEstadoEnvioInAndProximoIntentoLessThanEqualOrderByFechaCreacionAsc(
		// 				estados,
		// 				fechaLimite,
		// 				PageRequest.of(
		// 						0,
		// 						maximoResultados
		// 				)
		// 		)
		// 		.stream()
		// 		.map(EventoPendienteEntidad::getId)
		// 		.toList();

		List<EventoPendienteEntidad> entidades =
				eventoPendienteJpaRepositorio
						.findByEstadoEnvioInAndProximoIntentoLessThanEqualOrderByFechaCreacionAsc(
								estados,
								fechaLimite,
								PageRequest.of(
										0,
										maximoResultados
								)
						);

		List<Long> identificadores =
				new ArrayList<>();

		for (EventoPendienteEntidad entidad :
				entidades) {

			identificadores.add(entidad.getId());
		}

		return identificadores;

	}
}