package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PlatoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.CrearPlatoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.mapeador.PlatoDtoMapeador;
import co.edu.clase.microservicio_plazoleta.dominio.api.CrearPlatoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import org.springframework.stereotype.Component;

@Component
public class CrearPlatoHandler {

    private final CrearPlatoServicioPuerto
            crearPlatoServicioPuerto;

    private final PlatoDtoMapeador platoDtoMapeador;

    public CrearPlatoHandler(
            CrearPlatoServicioPuerto
                    crearPlatoServicioPuerto,
            PlatoDtoMapeador platoDtoMapeador) {

        this.crearPlatoServicioPuerto =
                crearPlatoServicioPuerto;

        this.platoDtoMapeador = platoDtoMapeador;
    }

    public PlatoRespuestaDto ejecutar(
            CrearPlatoSolicitudDto solicitud,
            Long propietarioAutenticadoId) {

        Plato plato = platoDtoMapeador.aModelo(solicitud);

        Plato creado = crearPlatoServicioPuerto.crear(
                plato,
                propietarioAutenticadoId
        );

        return platoDtoMapeador.aRespuesta(creado);
    }
}