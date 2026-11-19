package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.RestauranteValidacionRespuestaDto;
import co.edu.clase.microservicio_plazoleta.dominio.api.ValidarRestaurantePropietarioServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.RestauranteValidacion;
import org.springframework.stereotype.Component;

@Component
public class ValidarRestaurantePropietarioHandler {

    private final ValidarRestaurantePropietarioServicioPuerto
            validarRestaurantePropietarioServicioPuerto;

    public ValidarRestaurantePropietarioHandler(
            ValidarRestaurantePropietarioServicioPuerto
                    validarRestaurantePropietarioServicioPuerto) {

        this.validarRestaurantePropietarioServicioPuerto =
                validarRestaurantePropietarioServicioPuerto;
    }

    public RestauranteValidacionRespuestaDto ejecutar(
            Long restauranteId,
            Long propietarioAutenticadoId) {

        RestauranteValidacion resultado =
                validarRestaurantePropietarioServicioPuerto
                        .validar(
                                restauranteId,
                                propietarioAutenticadoId
                        );

        return new RestauranteValidacionRespuestaDto(
                resultado.getId(),
                resultado.getPropietarioId(),
                resultado.getActivo()
        );
    }
}