package co.edu.clase.microservicio_plazoleta.dominio.casouso;

import co.edu.clase.microservicio_plazoleta.dominio.api.ValidarRestaurantePropietarioServicioPuerto;
import co.edu.clase.microservicio_plazoleta.dominio.excepcion.ExcepcionPlazoleta;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.Restaurante;
import co.edu.clase.microservicio_plazoleta.dominio.modelo.RestauranteValidacion;
import co.edu.clase.microservicio_plazoleta.dominio.spi.RestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_plazoleta.util.constantes.CodigosError;
import co.edu.clase.microservicio_plazoleta.util.constantes.MensajesError;
import co.edu.clase.microservicio_plazoleta.util.enumeraciones.CategoriaError;

public class ValidarRestaurantePropietarioCasoUso
        implements ValidarRestaurantePropietarioServicioPuerto {

    private final RestaurantePersistenciaPuerto
            restaurantePersistenciaPuerto;

    public ValidarRestaurantePropietarioCasoUso(
            RestaurantePersistenciaPuerto
                    restaurantePersistenciaPuerto) {

        this.restaurantePersistenciaPuerto =
                restaurantePersistenciaPuerto;
    }

    @Override
    public RestauranteValidacion validar(
            Long restauranteId,
            Long propietarioAutenticadoId) {

        Restaurante restaurante =
                restaurantePersistenciaPuerto
                        .buscarPorId(restauranteId)
                        .orElseThrow(() ->
                                new ExcepcionPlazoleta(
                                        CodigosError
                                                .RESTAURANTE_NO_ENCONTRADO,
                                        MensajesError
                                                .RESTAURANTE_NO_ENCONTRADO,
                                        CategoriaError
                                                .NO_ENCONTRADO
                                )
                        );

        validarActivo(restaurante);
        validarPropiedad(
                restaurante,
                propietarioAutenticadoId
        );

        return new RestauranteValidacion(
                restaurante.getId(),
                restaurante.getPropietarioId(),
                restaurante.getActivo()
        );
    }

    private void validarActivo(Restaurante restaurante) {
        if (!Boolean.TRUE.equals(
                restaurante.getActivo()
        )) {
            throw new ExcepcionPlazoleta(
                    CodigosError.RESTAURANTE_INACTIVO,
                    MensajesError.RESTAURANTE_INACTIVO,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarPropiedad(
            Restaurante restaurante,
            Long propietarioAutenticadoId) {

        if (!restaurante.getPropietarioId()
                .equals(propietarioAutenticadoId)) {

            throw new ExcepcionPlazoleta(
                    CodigosError
                            .RESTAURANTE_NO_PERTENECE_PROPIETARIO,
                    MensajesError
                            .RESTAURANTE_NO_PERTENECE_PROPIETARIO,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }
}
