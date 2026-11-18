package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PlatoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.CambiarEstadoPlatoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.mapeador.PlatoDtoMapeador;
import co.edu.clase.microservicio_plazoleta.dominio.api.CambiarEstadoPlatoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import org.springframework.stereotype.Component;

@Component
public class CambiarEstadoPlatoHandler {

    private final CambiarEstadoPlatoServicioPuerto
            cambiarEstadoPlatoServicioPuerto;

    private final PlatoDtoMapeador platoDtoMapeador;

    public CambiarEstadoPlatoHandler(
            CambiarEstadoPlatoServicioPuerto
                    cambiarEstadoPlatoServicioPuerto,
            PlatoDtoMapeador platoDtoMapeador) {

        this.cambiarEstadoPlatoServicioPuerto =
                cambiarEstadoPlatoServicioPuerto;

        this.platoDtoMapeador = platoDtoMapeador;
    }

    public PlatoRespuestaDto ejecutar(
            Long platoId,
            CambiarEstadoPlatoSolicitudDto solicitud,
            Long propietarioAutenticadoId) {

        Plato resultado =
                cambiarEstadoPlatoServicioPuerto
                        .cambiarEstado(
                                platoId,
                                solicitud.getActivo(),
                                propietarioAutenticadoId
                        );

        return platoDtoMapeador.aRespuesta(resultado);
    }
}