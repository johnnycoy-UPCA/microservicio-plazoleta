package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.dominio.api.ProcesarEventoPendienteServicioPuerto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProcesarEventoPendienteHandler {

    private final ProcesarEventoPendienteServicioPuerto
            procesarEventoPendienteServicioPuerto;

    public ProcesarEventoPendienteHandler(
            ProcesarEventoPendienteServicioPuerto
                    procesarEventoPendienteServicioPuerto) {

        this.procesarEventoPendienteServicioPuerto =
                procesarEventoPendienteServicioPuerto;
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW
    )
    public void ejecutar(Long eventoId) {
        procesarEventoPendienteServicioPuerto
                .procesar(eventoId);
    }
}