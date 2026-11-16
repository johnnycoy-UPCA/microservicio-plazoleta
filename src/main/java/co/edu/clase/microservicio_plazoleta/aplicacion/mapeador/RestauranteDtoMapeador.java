package co.edu.clase.microservicio_plazoleta.aplicacion.mapeador;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.RestauranteRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.CrearRestauranteSolicitudDto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import org.springframework.stereotype.Component;

@Component
public class RestauranteDtoMapeador {

    public Restaurante aModelo(
            CrearRestauranteSolicitudDto solicitud) {

        Restaurante restaurante = new Restaurante();

        restaurante.setNombre(solicitud.getNombre());
        restaurante.setNit(solicitud.getNit());
        restaurante.setDireccion(solicitud.getDireccion());
        restaurante.setCelular(solicitud.getCelular());
        restaurante.setUrlLogo(solicitud.getUrlLogo());
        restaurante.setPropietarioId(
                solicitud.getPropietarioId()
        );

        return restaurante;
    }

    public RestauranteRespuestaDto aRespuesta(
            Restaurante restaurante) {

        RestauranteRespuestaDto respuesta =
                new RestauranteRespuestaDto();

        respuesta.setId(restaurante.getId());
        respuesta.setNombre(restaurante.getNombre());
        respuesta.setNit(restaurante.getNit());
        respuesta.setDireccion(restaurante.getDireccion());
        respuesta.setCelular(restaurante.getCelular());
        respuesta.setUrlLogo(restaurante.getUrlLogo());
        respuesta.setPropietarioId(
                restaurante.getPropietarioId()
        );
        respuesta.setActivo(restaurante.getActivo());
        respuesta.setFechaCreacion(
                restaurante.getFechaCreacion()
        );

        return respuesta;
    }
}