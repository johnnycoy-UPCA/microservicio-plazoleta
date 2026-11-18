package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.PlatoRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.ModificarPlatoSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.mapeador.PlatoDtoMapeador;
import co.edu.clase.microservicio_plazoleta.dominio.api.ModificarPlatoServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Plato;
import org.springframework.stereotype.Component;

@Component
public class ModificarPlatoHandler {

    private final ModificarPlatoServicioPuerto
            modificarPlatoServicioPuerto;

    private final PlatoDtoMapeador platoDtoMapeador;

    public ModificarPlatoHandler(
            ModificarPlatoServicioPuerto
                    modificarPlatoServicioPuerto,
            PlatoDtoMapeador platoDtoMapeador) {

        this.modificarPlatoServicioPuerto =
                modificarPlatoServicioPuerto;

        this.platoDtoMapeador = platoDtoMapeador;
    }

    public PlatoRespuestaDto ejecutar(
            Long platoId,
            ModificarPlatoSolicitudDto solicitud,
            Long propietarioAutenticadoId) {

        Plato platoModificado =
                modificarPlatoServicioPuerto.modificar(
                        platoId,
                        solicitud.getPrecio(),
                        solicitud.getDescripcion(),
                        propietarioAutenticadoId
                );

        return platoDtoMapeador.aRespuesta(
                platoModificado
        );
    }
}
