package co.edu.clase.microservicio_plazoleta.aplicacion.handler;

import co.edu.clase.microservicio_plazoleta.aplicacion.dto.respuesta.RestauranteRespuestaDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.dto.solicitud.CrearRestauranteSolicitudDto;
import co.edu.clase.microservicio_plazoleta.aplicacion.mapeador.RestauranteDtoMapeador;
import co.edu.clase.microservicio_plazoleta.dominio.api.CrearRestauranteServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import org.springframework.stereotype.Component;

@Component
public class CrearRestauranteHandler {

    private final CrearRestauranteServicioPuerto
            crearRestauranteServicioPuerto;

    private final RestauranteDtoMapeador
            restauranteDtoMapeador;

    public CrearRestauranteHandler(
            CrearRestauranteServicioPuerto
                    crearRestauranteServicioPuerto,
            RestauranteDtoMapeador
                    restauranteDtoMapeador) {

        this.crearRestauranteServicioPuerto =
                crearRestauranteServicioPuerto;

        this.restauranteDtoMapeador =
                restauranteDtoMapeador;
    }

    public RestauranteRespuestaDto ejecutar(
            CrearRestauranteSolicitudDto solicitud) {

        Restaurante restaurante =
                restauranteDtoMapeador.aModelo(solicitud);

        Restaurante creado =
                crearRestauranteServicioPuerto.crear(
                        restaurante
                );

        return restauranteDtoMapeador.aRespuesta(creado);
    }
}