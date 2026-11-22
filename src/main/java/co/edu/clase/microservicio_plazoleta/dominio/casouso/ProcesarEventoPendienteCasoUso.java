package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.ProcesarEventoPendienteServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoPendiente;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoPendientePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.EventoSerializadorPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.spi.TrazabilidadRegistroPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.EstadoEnvioEvento;

import java.time.Clock;
import java.time.Instant;

public class ProcesarEventoPendienteCasoUso
        implements ProcesarEventoPendienteServicioPuerto {

    private final EventoPendientePersistenciaPuerto
            eventoPendientePersistenciaPuerto;

    private final EventoSerializadorPuerto
            eventoSerializadorPuerto;

    private final TrazabilidadRegistroPuerto
            trazabilidadRegistroPuerto;

    private final Clock reloj;

    public ProcesarEventoPendienteCasoUso(
            EventoPendientePersistenciaPuerto
                    eventoPendientePersistenciaPuerto,
            EventoSerializadorPuerto
                    eventoSerializadorPuerto,
            TrazabilidadRegistroPuerto
                    trazabilidadRegistroPuerto,
            Clock reloj) {

        this.eventoPendientePersistenciaPuerto =
                eventoPendientePersistenciaPuerto;

        this.eventoSerializadorPuerto =
                eventoSerializadorPuerto;

        this.trazabilidadRegistroPuerto =
                trazabilidadRegistroPuerto;

        this.reloj = reloj;
    }

    @Override
    public void procesar(Long eventoId) {
        EventoPendiente eventoPendiente =
                obtenerEvento(eventoId);

        if (EstadoEnvioEvento.ENVIADO.equals(
                eventoPendiente.getEstadoEnvio()
        )) {
            return;
        }

        try {
            EventoTrazabilidad evento =
                    eventoSerializadorPuerto
                            .deserializar(
                                    eventoPendiente
                                            .getCargaJson()
                            );

            trazabilidadRegistroPuerto.registrar(evento);

            marcarEnviado(eventoPendiente);
        } catch (Exception excepcion) {
            registrarFallo(
                    eventoPendiente,
                    excepcion
            );
        }

        eventoPendientePersistenciaPuerto.guardar(
                eventoPendiente
        );
    }

    private EventoPendiente obtenerEvento(
            Long eventoId) {

        return eventoPendientePersistenciaPuerto
                .buscarPorId(eventoId)
                .orElseThrow(() ->
                        new ExcepcionPlazoleta(
                                CodigosError
                                        .EVENTO_PENDIENTE_NO_ENCONTRADO,
                                MensajesError
                                        .EVENTO_PENDIENTE_NO_ENCONTRADO,
                                CategoriaError
                                        .NO_ENCONTRADO
                        )
                );
    }

    private void marcarEnviado(
            EventoPendiente eventoPendiente) {

        Instant fechaActual = reloj.instant();

        eventoPendiente.setEstadoEnvio(
                EstadoEnvioEvento.ENVIADO
        );
        eventoPendiente.setFechaEnvio(fechaActual);
        eventoPendiente.setProximoIntento(
                fechaActual
        );
        eventoPendiente.setUltimoError(null);
    }

    private void registrarFallo(
            EventoPendiente eventoPendiente,
            Exception excepcion) {

        int intentosActualizados =
                eventoPendiente.getIntentos() + 1;

        eventoPendiente.setIntentos(
                intentosActualizados
        );
        eventoPendiente.setFechaEnvio(null);
        eventoPendiente.setUltimoError(
                resumirError(excepcion)
        );

        if (intentosActualizados
                >= ConstantesValidacion
                .OUTBOX_MAXIMO_INTENTOS) {

            eventoPendiente.setEstadoEnvio(
                    EstadoEnvioEvento.ERROR
            );

            eventoPendiente.setProximoIntento(
                    Instant.MAX
            );
            return;
        }

        eventoPendiente.setEstadoEnvio(
                EstadoEnvioEvento.PENDIENTE
        );

        long esperaSegundos =
                calcularEsperaSegundos(
                        intentosActualizados
                );

        eventoPendiente.setProximoIntento(
                reloj.instant().plusSeconds(
                        esperaSegundos
                )
        );
    }

    private long calcularEsperaSegundos(
            int numeroIntento) {

        long multiplicador =
                1L << Math.min(
                        numeroIntento - 1,
                        20
                );

        long espera =
                ConstantesValidacion
                        .OUTBOX_REINTENTO_BASE_SEGUNDOS
                        * multiplicador;

        return Math.min(
                espera,
                ConstantesValidacion
                        .OUTBOX_REINTENTO_MAXIMO_SEGUNDOS
        );
    }

    private String resumirError(
            Exception excepcion) {

        String mensaje = excepcion.getMessage();

        if (mensaje == null || mensaje.isBlank()) {
            mensaje = excepcion.getClass()
                    .getSimpleName();
        }

        int longitudMaxima = 500;

        if (mensaje.length() <= longitudMaxima) {
            return mensaje;
        }

        return mensaje.substring(0, longitudMaxima);
    }
}