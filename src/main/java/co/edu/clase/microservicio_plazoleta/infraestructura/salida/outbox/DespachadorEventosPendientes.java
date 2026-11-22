package co.edu.clase.microservicio_plazoleta.infraestructura.salida.outbox;

import co.edu.clase.microservicio_plazoleta.aplicacion.handler.ProcesarEventoPendienteHandler;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoPendientePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;

@Component
@Profile("!prueba")
public class DespachadorEventosPendientes {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    DespachadorEventosPendientes.class
            );

    private static final List<EstadoEnvioEvento>
            ESTADOS_PROCESABLES =
            List.of(EstadoEnvioEvento.PENDIENTE);

    private final EventoPendientePersistenciaPuerto
            eventoPendientePersistenciaPuerto;

    private final ProcesarEventoPendienteHandler
            procesarEventoPendienteHandler;

    private final Clock reloj;

    public DespachadorEventosPendientes(
            EventoPendientePersistenciaPuerto eventoPendientePersistenciaPuerto,
            ProcesarEventoPendienteHandler procesarEventoPendienteHandler,
            Clock reloj) {

        this.eventoPendientePersistenciaPuerto = eventoPendientePersistenciaPuerto;
        this.procesarEventoPendienteHandler = procesarEventoPendienteHandler;
        this.reloj = reloj;
    }

    @Scheduled(
            fixedDelayString = "${outbox.despachador.intervalo-ms:10000}",
            initialDelayString = "${outbox.despachador.demora-inicial-ms:10000}"
    )
    public void despachar() {
        List<Long> eventoIds =
                eventoPendientePersistenciaPuerto
					.buscarIdsParaProcesar(
						ESTADOS_PROCESABLES,
						reloj.instant(),
						ConstantesValidacion.OUTBOX_TAMANO_LOTE
					);

        if (eventoIds.isEmpty()) {
            return;
        }

        LOGGER.info(
			"Outbox: procesando {} evento(s)",
			eventoIds.size()
        );

        for (Long eventoId : eventoIds) {
            procesarEvento(
                    eventoId
            );
        }
    }

    private void procesarEvento(Long eventoId) {
        try {
            procesarEventoPendienteHandler
                    .ejecutar(eventoId);
        } catch (Exception excepcion) {
            LOGGER.error(
				"Outbox: error inesperado "
				+ "procesando eventoId={}",
				eventoId,
				excepcion
            );
        }
    }
}